package com.uni.uni_erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "user/signIn";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "user/signUp";
    }

}
