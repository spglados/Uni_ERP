package com.uni.uni_erp.controller;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HttpSession session;

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

    // 변경사항

}
