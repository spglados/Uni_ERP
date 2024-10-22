package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
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

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
    private final ScheduleService scheduleService;
    private final HttpSession session;

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
        hrService.registerEmployee(employeeDTO, storeId);
        return "redirect:/erp/hr/employee-list";
    }

    @GetMapping("/employee-list")
    public String employeeListPage(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<EmployeeDTO> employeeDTOList = hrService.getEmployeesByStoreId(storeId); // EmployeeDTO로 변경

        // 직원 목록의 내용 확인
        for (EmployeeDTO dto : employeeDTOList) {
            System.out.println("EmployeeDTO: " + dto); // 각 DTO 출력
        }

        model.addAttribute("employees", employeeDTOList); // 직원 목록을 모델에 추가

        // DTO 리스트를 JSON으로 변환
        String employeesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            employeesJson = objectMapper.writeValueAsString(employeeDTOList);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외의 상세 정보 출력
            throw new RuntimeException("직원 목록을 JSON으로 변환하는 중 오류 발생: " + e.getMessage(), e);
        }

        model.addAttribute("employeesJson", employeesJson); // JSON 데이터를 모델에 추가

        return "erp/hr/employeeList"; // 직원 목록 페이지 반환
    }

    @GetMapping("/schedule")
    public String schedulePage(@RequestParam(name = "type", required = false) String type, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 문자열로 받은 type을 enum 타입으로 변환
        Schedule.ScheduleType scheduleType = EnumCommonUtil.getEnumFromString(Schedule.ScheduleType.class, type);

        // 일정 조회
        List<ScheduleDTO.ResponseDTO> schedules = scheduleService.findByStoreIdAndType(storeId, scheduleType);

        // 직원 목록을 EmployeeDTO로 가져오기
        List<EmployeeDTO> employees = hrService.getEmployeesByStoreId(storeId);

        String schedulesJson;
        try {
            schedulesJson = new ObjectMapper().writeValueAsString(schedules);
        } catch (JsonProcessingException e) {
            throw new Exception500("알 수 없는 오류 발생.");
        }
        model.addAttribute("schedules", schedulesJson);
        model.addAttribute("employees", employees);

        return "/erp/hr/schedule";
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleCreateProc(@RequestBody ScheduleDTO.CreateDTO reqDTO) {
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

}
