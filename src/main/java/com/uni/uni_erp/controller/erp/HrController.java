package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmpDocumentDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.dto.erp.hr.ScheduleDTO;
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
import java.util.stream.Collectors;

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
        System.out.println("storeId: " + storeId);
        hrService.registerEmployee(employeeDTO, storeId);
        return "redirect:/erp/hr/employee-list";
    }

    // TODO 스토어로 바꾸기
    @GetMapping("/employee-list")
    public String employeeListPage(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<Employee> employees = hrService.getEmployeesByStoreId(storeId);
        // 각 직원에 대한 문서 정보를 추가
        // 각 직원에 대한 문서 정보를 추가
        List<EmpDocumentDTO> empDocuments = employees.stream()
                .map(employee -> {
                    List<EmpDocument> documents = hrService.getEmpDocumentsByEmployeeId(employee.getId());
                    // 첫 번째 문서 정보를 가져와 DTO로 변환 (필요에 따라 수정)
                    EmpDocumentDTO empDocumentDTO = new EmpDocumentDTO();
                    empDocumentDTO.setEmployeeId(employee.getId());
                    // 문서 정보를 개별적으로 설정
                    if (!documents.isEmpty()) {
                        EmpDocument document = documents.get(0); // 첫 번째 문서만 가져오도록 설정 (예시)
                        empDocumentDTO.setEmploymentContract(document.getEmploymentContract());
                        empDocumentDTO.setHealthCertificate(document.getHealthCertificate());
                        empDocumentDTO.setIdentificationCopy(document.getIdentificationCopy());
                        empDocumentDTO.setBankAccountCopy(document.getBankAccountCopy());
                        empDocumentDTO.setResidentRegistration(document.getResidentRegistration());
                    }
                    return empDocumentDTO;
                }).collect(Collectors.toList());


        model.addAttribute("employees", employees); // 직원 목록을 모델에 추가
        model.addAttribute("empDocuments", empDocuments); // 문서 정보를 모델에 추가

        return "erp/hr/employeeList"; // 직원 목록 페이지 반환
    }

    /**
     * 근무 일정 관리 페이지 호출
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/schedule")
    public String schedulePage(@RequestParam(name = "type", required = false) String type, Model model) throws JsonProcessingException {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 문자열로 받은 type을 enum 타입으로 변환
        Schedule.ScheduleType scheduleType = EnumCommonUtil.getEnumFromString(Schedule.ScheduleType.class, type);

        // 일정 조회
        List<ScheduleDTO.ResponseDTO> schedules = scheduleService.findByStoreIdAndType(storeId, scheduleType);

        // 모든 근무자 조회
        hrService.getEmployeesByStoreId(storeId);
        String schedulesJson = new ObjectMapper().writeValueAsString(schedules);
        model.addAttribute("schedules", schedulesJson);

        return "/erp/hr/schedule";
    }

    /**
     * 근무 일정 등록
     * @param reqDTO
     * @return
     */
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
