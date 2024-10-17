package com.uni.uni_erp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Testcontroller {

    @GetMapping("/p")
    public String testPage() {
        return "erp/product/register";
    }

}
