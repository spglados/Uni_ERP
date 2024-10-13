package com.uni.uni_erp.controller.erp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp/hr")
public class HrController {

    @GetMapping("/employeeRegister")
    public String employeeRegisterPage() {
        return "/erp/hr/employeeRegister";
    }

    @GetMapping("/employeeList")
    public String employeeListPage() {
        return "/erp/hr/employeeList";
    }

}
