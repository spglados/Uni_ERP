package com.uni.uni_erp.controller.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.service.user.StoreService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StoreService storeService;
    private final HttpSession session;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO.JoinDTO dto) {
        // TODO 유효성 검사 추가
        User user = userService.login(dto);
        List<Integer> storeIdList = storeService.ownedStores(user.getId());

        if(storeIdList != null) {
            // 맨 처음 가게 아이디 추가
            session.setAttribute("storeId", storeIdList.get(0));
        }

        session.setAttribute("userSession", user);
        if (user != null) {
            session.setAttribute("userSession", user);
            System.out.println("User logged in: " + user.getId());
        } else {
            System.out.println("Login failed");
        }
        return "main"; // 로그인 후 이동할 페이지
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

}
