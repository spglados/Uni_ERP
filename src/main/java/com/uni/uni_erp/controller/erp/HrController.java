package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.erp.hr.*;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.service.erp.hr.AttendanceService;
import com.uni.uni_erp.service.erp.hr.HrService;
import com.uni.uni_erp.service.erp.hr.PayrollService;
import com.uni.uni_erp.service.erp.hr.ScheduleService;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import com.uni.uni_erp.util.Str.GsonUtil;
import com.uni.uni_erp.util.define.Define_HR;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;
    private final PayrollService payrollService;
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
        try {
            EmployeeDTO dto = hrService.updateEmployee(id, employeeDTO);
            if (dto != null) {
                return ResponseEntity.ok(Map.of("success", "직원 정보 수정완료"));
            } else {
                return ResponseEntity.ok(Map.of("fail", "중복된 정보가 있습니다."));
            }
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

    public ResponseEntity<Map<String, Object>> checkAccountNumber(@RequestParam String accountNumber, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Map<String, Object> response = new HashMap<>();
        boolean isDuplicate = hrService.isAccountNumberDuplicated(accountNumber, storeId);
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    // 중복 이메일 검사
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Map<String, Object> response = new HashMap<>();
        boolean isDuplicated = hrService.isEmailDuplicated(email, storeId);
        response.put("isDuplicated", isDuplicated);
        return ResponseEntity.ok(response);
    }

    // 중복 전화번호 검사
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestParam String phone, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        Map<String, Object> response = new HashMap<>();
        boolean isDuplicated = hrService.isPhoneDuplicated(phone, storeId);
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
     * 근태 관리 페이지 요청
     *
     * @param startDate 날짜 필터
     * @param endDate   날짜 필터
     * @param session   현재 상점 확인용
     * @param model     근태 리스트 추가
     * @return jsp
     */
    @GetMapping("/attendance-list")
    public String attendanceListPage(@RequestParam(name = "startDate", required = false) LocalDate startDate, @RequestParam(name = "endDate", required = false) LocalDate endDate, HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<AttendanceDTO.GridDTO> dataList = attendanceService.findAttendanceList(storeId, startDate, endDate);
        String dataJson = GsonUtil.convertToJson(dataList);
        System.out.println(dataJson);
        model.addAttribute(Define_HR.DATALIST, dataJson);
        return "erp/hr/attendanceList";
    }

    /**
     * 사번으로 조회 요청
     *
     * @param uniqueEmployeeNumber 사번
     * @param session              현재 상점 확인용
     * @return 조회된 근무 리스트 및 사원 이름 반환
     */
    @GetMapping("/attendance/{uniqueEmployeeNumber}")
    public ResponseEntity<?> attendanceRequest(@PathVariable(name = "uniqueEmployeeNumber") Long uniqueEmployeeNumber, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<AttendanceDTO.ResponseDTO> resDTO = attendanceService.findSchedulesByUniqueEmployeeNumber(uniqueEmployeeNumber, storeId);
        Map<String, Object> response = new HashMap<>();
        response.put(Define_HR.DATALIST, resDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * 사번으로 출퇴근 요청
     *
     * @param uniqueEmployeeNumber 사번
     * @param reqDTO               출퇴근 여부 및 비밀번호
     * @param session              현재 상점 확인용
     * @return 업데이트된 근무 리스트 및 사원 이름 반환
     */
    @PutMapping("/attendance/{uniqueEmployeeNumber}")
    public ResponseEntity<?> attendanceProc(@PathVariable(name = "uniqueEmployeeNumber") Long uniqueEmployeeNumber, @RequestBody AttendanceDTO.RequestDTO reqDTO, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<AttendanceDTO.ResponseDTO> resDTO = attendanceService.attendanceProc(uniqueEmployeeNumber, storeId, reqDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("dataList", resDTO);
        return ResponseEntity.ok(response);
    }


    /**
     * 근무 일정 관리 페이지 호출
     *
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
     *
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
     *
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
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 급여 산출 페이지
     *
     * @param model 직원 정보 저장
     * @return 급여 산출 페이지
     */
    @GetMapping("/salaries-calculator")
    public String salaryCalculatorPage(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<EmployeeDTO> employees = payrollService.getEmployeesNoCalculated(storeId);
        model.addAttribute("employees", employees);
        return "/erp/hr/salariesCalculator";
    }

    /**
     * 급여 계산 요청
     *
     * @param reqDTO 산출 대상과 수당 및 세금 포함 여부 조회
     * @return 계산 결과 반환
     */
    @GetMapping("/salaries-calculator/employee")
    public ResponseEntity<?> salaryCalculate(@ModelAttribute PayrollDTO.CalculateDTO reqDTO) {
        List<PayrollDTO.CalculateResultDTO> resDTO = payrollService.calculateGrossSalary(reqDTO.getEmpNos(), reqDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDTO);
    }

    /**
     * 계산된 급여 저장
     *
     * @param reqDTO 계산된 급여에서 수당 및 세금 여부 선택 받아서 저장
     * @return 성공 여부 반환
     */
    @PostMapping("/salaries-calculator")
    public ResponseEntity<?> payrollProc(@RequestBody PayrollDTO.CreateDTO reqDTO) {
        Map<String, Object> response = new HashMap<>();
        if (payrollService.create(reqDTO)) {
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/salaries-history")
    public String empSalaryPage(Long empNo, String yearMonth, Model model, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        // 기본값 설정 (현재 월)
        YearMonth currentYearMonth = (yearMonth != null && !yearMonth.isEmpty()) ? YearMonth.parse(yearMonth) : YearMonth.now().minusMonths(1);
        model.addAttribute("currentYearMonth", currentYearMonth.toString());
        List<SalaryDTO> employeeList = payrollService.getPayrollByStoreId(storeId, currentYearMonth);
        model.addAttribute("employeeList", employeeList);
        if (employeeList.isEmpty()) {
            return "/erp/hr/salariesHistory";
        }

        // 선택된 직원 ID가 없으면 첫 번째 직원 선택
        if (empNo == null && !employeeList.isEmpty()) {
            empNo = employeeList.get(0).getEmpNo();
        }

        // 선택된 직원 찾기
        // 람다 사용하려면 간접적 파이널 혹은 파이널 변수 써야됨.
        Long finalEmployeeId = empNo;
        Optional<SalaryDTO> selectedEmployeeOpt = employeeList.stream()
                .filter(emp -> emp.getEmpNo() == finalEmployeeId)
                .findFirst();

        if (selectedEmployeeOpt.isPresent()) {
            SalaryDTO selectedEmployee = selectedEmployeeOpt.get();
            // 해당 월의 급여 상세 찾기
            Optional<SalaryDTO.SalaryDetail> salaryDetailOpt = selectedEmployee.getSalaryDetails().getYear() == currentYearMonth.getYear() &&
                    selectedEmployee.getSalaryDetails().getMonth() == currentYearMonth.getMonthValue()
                    ? Optional.of(selectedEmployee.getSalaryDetails())
                    : Optional.empty();

            SalaryDTO.SalaryDetail salaryDetail = salaryDetailOpt.orElse(new SalaryDTO.SalaryDetail());

            model.addAttribute("selectedEmployee", selectedEmployee);
            model.addAttribute("salaryDetail", salaryDetail);
        } else {
            model.addAttribute("selectedEmployee", new SalaryDTO());
            model.addAttribute("salaryDetail", new SalaryDTO.SalaryDetail());
        }

        return "/erp/hr/salariesHistory";
    }

}