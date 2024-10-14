package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.service.erp.HrService;
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

    @GetMapping("/employeeRegister")
    public String employeeRegisterPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "/erp/hr/employeeRegister";
    }

    @PostMapping("/register")
    public String registerEmployee(@RequestBody EmployeeDTO employeeDTO, @SessionAttribute("user") User owner) {
        hrService.registerEmployee(employeeDTO, owner);
        return "직원이 등록되었습니다.";
    }

    @GetMapping("/employeeList")
    public String employeeListPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("userSession"); // 세션에서 사용자 정보 가져오기
        if (loggedInUser != null) {
            System.out.println("Logged in user ID: " + loggedInUser.getId()); // 로그 추가
            List<Employee> employees = hrService.getEmployeesByUserId(loggedInUser.getId());
            model.addAttribute("employees", employees); // 직원 목록을 모델에 추가
        } else {
            System.out.println("No user logged in"); // 로그 추가
        }
        return "/erp/hr/employeeList"; // 직원 목록 페이지 반환
    }

}
