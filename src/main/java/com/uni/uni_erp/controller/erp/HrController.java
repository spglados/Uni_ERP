package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.service.erp.hr.HrService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
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


}
