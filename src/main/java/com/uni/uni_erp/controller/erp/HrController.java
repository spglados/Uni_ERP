package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.erp.hr.EmpPositionDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeUpdateDTO;
import com.uni.uni_erp.dto.erp.hr.ScheduleDTO;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.service.erp.hr.HrService;
import com.uni.uni_erp.service.erp.hr.ScheduleService;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
    private final ScheduleService scheduleService;
    private final HttpSession session;

    @GetMapping("/download/excel")
    public void downloadExcel(HttpSession session, @RequestParam(required = false) String employeeStatus, HttpServletResponse response) {
        // 상태값 출력 확인
        System.out.println("Employee Status: " + employeeStatus);

        Integer storeId = (Integer) session.getAttribute("storeId");
        hrService.downloadEmployeeExcel(storeId, employeeStatus, response);
    }

    // 직원 수정
    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeUpdateDTO employeeDTO) {
        System.out.println("id :" + id);
        System.out.println("Received DTO: " + employeeDTO);
        try {
            hrService.updateEmployee(id, employeeDTO);
            return ResponseEntity.ok("직원 정보 수정완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 직원 등록 페이지 이동
    @GetMapping("/employee-register")
    public String employeeRegisterPage(Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<BankDTO> bankList = hrService.getAllBankDTOs();
        List<EmpPositionDTO> positionsList = hrService.getPositionsByStoreId(storeId); // 직책 목록 추가
        model.addAttribute("bankList", bankList);
        model.addAttribute("positionsList", positionsList); // 모델에 직책 추가
        model.addAttribute("employee", new Employee());
        model.addAttribute("storeId", storeId); // storeId를 모델에 추가
        return "/erp/hr/employeeRegister";
    }

    // 직원 등록
    @PostMapping("/registerEmployee")
    public String registerEmployee(@ModelAttribute EmployeeDTO employeeDTO, @RequestParam Integer storeId, Model model, HttpSession session) {
        // TODO UserDTO로 변경 필
        String email = employeeDTO.getEmail() + "@" + employeeDTO.getEmailDomain();
        employeeDTO.setEmail(email);


        User user = (User) session.getAttribute("userSession");
        try {
            hrService.registerEmployee(employeeDTO, storeId, user.getId());
            return "redirect:/erp/hr/employee-list"; // 등록 성공 시 직원 리스트로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employeeDTO", employeeDTO); // 입력한 데이터 유지
            return "forward:/erp/hr/employee-register"; // 등록 폼으로 돌아감
        }
    }

    // 중복 이메일 검사
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        boolean isDuplicated = hrService.isEmailDuplicated(email);
        response.put("isDuplicated", isDuplicated);
        return ResponseEntity.ok(response);
    }

    // 중복 전화번호 검사
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestParam String phone) {
        Map<String, Object> response = new HashMap<>();
        boolean isDuplicated = hrService.isPhoneDuplicated(phone);
        response.put("isDuplicated", isDuplicated);
        return ResponseEntity.ok(response);
    }

    // 직원 목록 조회
    @GetMapping("/employee-list")
    public String employeeListPage(@RequestParam(required = false) String status, HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        List<EmployeeDTO> employeeDTOList;

        // 상태가 있는 경우 해당 상태의 직원 목록을 스토어 ID로 필터링하여 조회
        if (status != null && !status.isEmpty()) {
            employeeDTOList = hrService.getEmployeesByStatusAndStoreId(status, storeId);
        } else {
            // 상태가 없을 경우 스토어 ID에 따른 모든 직원 목록 조회
            employeeDTOList = hrService.getEmployeesByStoreId(storeId);
        }

        // 모든 직책 목록 조회
        List<EmpPositionDTO> positionDTOList = hrService.getPositionsByStoreId(storeId);

        // 모든 은행 목록 조회
        List<BankDTO> bankDTOList = hrService.getAllBankDTOs();


        // 직원 목록의 내용 확인
//        for (EmployeeDTO dto : employeeDTOList) {
//            System.out.println("EmployeeDTO: " + dto); // 각 DTO 출력
//        }

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
        model.addAttribute("positions", positionDTOList);
        model.addAttribute("banks", bankDTOList);

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
        List<EmployeeDTO> employees = hrService.getEmployeesByStoreId(storeId);
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
