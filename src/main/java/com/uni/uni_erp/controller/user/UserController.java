package com.uni.uni_erp.controller.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.repository.payment.Sms;
import com.uni.uni_erp.service.common.EmailService;
import com.uni.uni_erp.service.user.StoreService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StoreService storeService;
    private final HttpSession session;
    private final EmailService emailService;
    private final com.uni.uni_erp.repository.payment.smsrepository smsrepository;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO.JoinDTO dto) {
        // TODO 유효성 검사 추가
        User user = userService.login(dto);
        List<StoreDTO> storeList = storeService.ownedStores(user.getId());

        if(storeList != null) {
            // 맨 처음 가게 아이디 추가
            session.setAttribute("storeId", storeList.get(0).getId());
            if(storeList.size() > 1) {
                session.setAttribute("storeList", storeList);
            }
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

    @GetMapping("/sendPhoneVerification")
    public String sendSMS(@RequestParam("phone") String userPhoneNumber) {
        int randomNumber = (int)((Math.random() * (9999 - 1000 + 1)) + 1000); // 난수 생성
        //TODO
        Sms sms = new Sms();
        sms.setRandomNumber(randomNumber);
        smsrepository.save(sms);
        //TODO
        //userService.certifiedPhoneNumber(userPhoneNumber, randomNumber);

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


    // 아이디 중복 확인
    @GetMapping("/checkId")
    public ResponseEntity<Map<String, String>> checkDuplicate(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();

        boolean isUse = userService.checkDuplicateEmail(email);

        if (isUse) {
            response.put("message", "중복된 이메일입니다.");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("message", "사용 가능한 이메일입니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkPhone")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestParam("phone") String phone) {
        boolean isUse = userService.checkDuplicatePhone(phone);
        Map<String, Object> response = new HashMap<>();

        if (isUse) {
            response.put("message", "이미 사용 중인 번호입니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "사용 가능한 번호입니다.");
            return ResponseEntity.ok(response);
        }
    }

}
