package com.uni.uni_erp.controller;

import com.uni.uni_erp.exception.errors.Exception404;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/introduction")
    public String introductionPage() {
        return "/common/introduction";
    }

    @GetMapping("/support")
    public String supportPage() {
        return "common/support";
    }

}
