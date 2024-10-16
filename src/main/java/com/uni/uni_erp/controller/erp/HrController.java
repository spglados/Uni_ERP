package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmpDocumentDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.service.erp.hr.HrService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
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


}
