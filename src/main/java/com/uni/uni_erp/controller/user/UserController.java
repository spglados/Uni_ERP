package com.uni.uni_erp.controller.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.service.common.EmailService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final HttpSession session;
    private final EmailService emailService;


    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO.JoinDTO dto) {
        User user = userService.login(dto);
        session.setAttribute("userSession", user);
        return "main";
    }

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO.JoinDTO dto) {
        userService.save(dto.toUserEntity());
        return "user/login";
    }

    @GetMapping("/sendPhoneVerification")
    public String sendSMS(@RequestParam("phone") String userPhoneNumber) {
        int randomNumber = (int)((Math.random() * (9999 - 1000 + 1)) + 1000); // 난수 생성

        userService.certifiedPhoneNumber(userPhoneNumber, randomNumber);

        // 인증번호를 세션에 저장
        session.setAttribute("verificationCode", randomNumber);

        return Integer.toString(randomNumber);
    }

    @PostMapping("/verifyPhone")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyPhone(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();

        // 세션에서 저장된 인증번호를 가져옵니다.
        Integer sessionCode = (Integer) session.getAttribute("verificationCode");

        // 인증번호가 세션에 저장된 것과 일치하는지 확인합니다.
        if (sessionCode != null && sessionCode.toString().equals(code)) {
            response.put("success", true);
        } else {
            response.put("success", false);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
