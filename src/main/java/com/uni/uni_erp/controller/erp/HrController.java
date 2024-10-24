package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmpDocumentDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.dto.erp.hr.ScheduleDTO;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.service.erp.hr.HrService;
import com.uni.uni_erp.service.erp.hr.ScheduleService;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
    private final ScheduleService scheduleService;
    private final HttpSession session;
    private final Gson Gson;


    @GetMapping("/employee-register")
    public String employeeRegisterPage(Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<BankDTO> bankList = hrService.getAllBankDTOs();
        model.addAttribute("bankList", bankList);
        model.addAttribute("employee", new Employee());
        model.addAttribute("storeId", storeId); // storeId를 모델에 추가
        return "/erp/hr/employeeRegister";
    }

    @PostMapping("/employee-register")
    public String registerEmployee(@ModelAttribute EmployeeDTO employeeDTO, @RequestParam(name = "storeId") Integer storeId) {
        System.out.println("storeId: " + storeId);
        hrService.registerEmployee(employeeDTO, storeId);
        return "redirect:/erp/hr/employee-list";
    }

    // TODO 스토어로 바꾸기
    @GetMapping("/employee-list")
    public String employeeListPage(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<EmployeeDTO> employeeDTOList = hrService.getEmployeesByStoreIdWithDocuments(storeId);
        model.addAttribute("employees", employeeDTOList); // 직원 목록을 모델에 추가
        model.addAttribute("employeesJson", Gson.toJson(employeeDTOList));
        return "erp/hr/employeeList"; // 직원 목록 페이지 반환
    }

    /**
     * 근무 일정 관리 페이지 호출
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/schedule")
    public String schedulePage(@RequestParam(name = "type", required = false) String type, Model model, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 문자열로 받은 type을 enum 타입으로 변환
        Schedule.Status scheduleType = EnumCommonUtil.getEnumFromString(Schedule.Status.class, type);

        // 일정 조회
        List<ScheduleDTO.ResponseDTO> schedules = scheduleService.findByStoreIdAndType(storeId, scheduleType);

        // TODO DTO로 변경해야함 모든 근무자 조회
        List<Employee> employees = hrService.getEmployeesByStoreId(storeId);
        List<Map<String, Object>> employeesMap = employees.stream()
                .map(employee -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("empId", employee.getId());
                    map.put("empName", employee.getName());
                    return map;
                })
                .collect(Collectors.toList());

        String schedulesJson = null;
        String employeesJson = null;
        try {
            schedulesJson = new ObjectMapper().writeValueAsString(schedules);
            employeesJson = new ObjectMapper().writeValueAsString(employeesMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Exception500("알 수 없는 오류 발생.");
        }
        model.addAttribute("schedulesJson", schedulesJson);
        model.addAttribute("employees", employees);
        model.addAttribute("employeesJson", employeesJson);

        return "/erp/hr/schedule";
    }

    /**
     * 근무 일정 등록
     * @param reqDTO
     * @return
     */
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleCreateProc(@RequestBody ScheduleDTO.CreateDTO reqDTO, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 일정 생성
        ScheduleDTO.ResponseDTO schedule = scheduleService.create(reqDTO, storeId);

        // 응답 데이터 추가
        Map<String, Object> response = new HashMap<>();
        if (schedule != null) {
            response.put("schedule", schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 근무 일정 수정
     * @param reqDTO
     * @return
     */
    @PutMapping("/schedule")
    public ResponseEntity<?> scheduleUpdateProc(@RequestBody ScheduleDTO.UpdateDTO reqDTO, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 일정 생성
        ScheduleDTO.ResponseDTO schedule = scheduleService.update(reqDTO, storeId);

        // 응답 데이터 추가
        Map<String, Object> response = new HashMap<>();
        if (schedule != null) {
            response.put("schedule", schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
