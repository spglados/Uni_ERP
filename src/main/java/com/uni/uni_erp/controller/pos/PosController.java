package com.uni.uni_erp.controller.pos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    /**
     * 포스 메인 페이지 요청
     * @return
     */
    @GetMapping("/main")
    public String showPosMainPage() {
        return "pos/posMain";  // posMain.css 화면 반환
    }

    /**
     * 가상 포스 결제 요청
     * @return
     */
    @PostMapping("/payment")
    public String posPayment() {

        return "pos/posMain";
    }
}
