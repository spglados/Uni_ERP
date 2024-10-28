package com.uni.uni_erp.controller.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.PrincipalDTO;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.repository.payment.Sms;
import com.uni.uni_erp.repository.payment.smsrepository;
import com.uni.uni_erp.service.common.EmailService;
import com.uni.uni_erp.service.user.StoreService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final StoreService storeService;
    private final HttpSession session;
    private final EmailService emailService;

    private final smsrepository smsrepository;


    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO.JoinDTO dto) {
        // TODO 유효성 검사 추가
        User user = userService.login(dto);
        PrincipalDTO principalDTO = userService.searchUserId(user.getId());
        session.setAttribute("principal", principalDTO);
        List<Integer> storeIdList = storeService.ownedStores(user.getId());

        if(storeIdList != null) {
            // 맨 처음 가게 아이디 추가
            session.setAttribute("storeId", storeIdList.get(1));
        }

        if (user != null) {
            session.setAttribute("userSession", user);
            System.out.println("User logged in: " + user.getId());
        } else {
            System.out.println("Login failed");
        }
        return "main"; // 로그인 후 이동할 페이지
    }


    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 세션 무효화
        return "redirect:/main"; // 메인 페이지로 리다이렉트
    }


    @GetMapping("/join")
    public String join(@SessionAttribute(value = "principal") PrincipalDTO principal) {
        int userPk = principal.getId();
        User user = userService.findById(userPk);

        if (user != null && user.isWithinWeekOfCommon()) {
            // POST 요청 처리 로직
            return "user/join"; // 성공 시 결과 페이지
        } else {
            // 오류 페이지 또는 메시지 반환
            return "/error"; // POST 요청이 거부된 경우
        }

    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<Map<String, String>> join(@RequestBody UserDTO.JoinDTO dto) {
        System.out.println(dto);
        //TODO - 추가한코드
        User user = userService.findById(1);

        if (user != null && user.getPreviousMembership() != null &&
                user.getPreviousMembership().equals("PREMIUM") &&
                user.getPremiumToCommonDate() != null &&
                user.getPremiumToCommonDate().isAfter(LocalDateTime.now().minusSeconds(1))) {
            // 1초 이내에 POST 요청이 들어오면 에러 응답 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원가입이 잠시 불가능합니다. 잠시 후 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse); // 403 Forbidden 응답 반환
        }
        ///
        userService.save(dto.toUserEntity());

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입이 완료되었습니다.");
        return ResponseEntity.ok(response); // 성공 응답 반환
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

