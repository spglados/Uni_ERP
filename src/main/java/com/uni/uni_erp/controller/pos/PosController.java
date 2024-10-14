package com.uni.uni_erp.controller.pos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pos")
public class PosController {

    @GetMapping("/main")
    public String showPosMainPage() {
        return "pos/posMain";  // posMain.css 화면 반환
    }

    @PostMapping("/pos")
    public String processOrder(@RequestParam String item, @RequestParam int quantity, @RequestParam String price) {

        return "pos";
    }
}
