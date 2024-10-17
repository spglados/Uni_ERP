package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.dto.BankDTO;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.service.erp.hr.HrService;
import com.uni.uni_erp.util.date.GsonUtil;
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
        // 스토어에 해당하는 직원 목록과 문서 정보를 동시에 가져옴
        List<Employee> employees = hrService.getEmployeesByStoreIdWithDocuments(storeId);
        JsonArray temp = new JsonArray();
        for (Employee employee : employees) {
            temp.addAll(GsonUtil.toJsonArray(employee.getEmpDocument()));
        }
        model.addAttribute("employeesJson", temp);
        model.addAttribute("employees", employees); // 직원 목록을 모델에 추가

        return "erp/hr/employeeList"; // 직원 목록 페이지 반환
    }


}
