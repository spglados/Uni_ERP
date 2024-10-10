package com.uni.uni_erp.controller.erp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp/product")
public class ProductController {

    @GetMapping("/list")
    public String listPage() {
        return "erp/product/list";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "erp/product/register";
    }
}
