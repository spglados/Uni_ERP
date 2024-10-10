package com.uni.uni_erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp")
public class ErpController {

    @GetMapping("/main")
    public String main() {
        return "erp/main";
    }

}
