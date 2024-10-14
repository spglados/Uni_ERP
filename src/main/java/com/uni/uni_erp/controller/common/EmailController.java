package com.uni.uni_erp.controller.common;

import com.uni.uni_erp.service.common.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시징을 위한 템플릿

    @PostMapping("/sendVerification")
    public String sendVerificationEmail(@RequestParam("email") String email) {
        // 토큰 생성
        String token = emailService.generateVerificationToken();

        // 이메일 전송
        emailService.sendVerificationEmail(email, token);

        return "인증 이메일이 전송되었습니다.";
    }

    @GetMapping("/validate")
    public String validateEmail(@RequestParam("token") String token) {
        if (emailService.validateToken(token)) {
            // 인증 성공 시 웹소켓을 통해 클라이언트로 전송
            messagingTemplate.convertAndSend("/topic/verify", "ok");
            return "인증 성공";
        } else {
            return "인증 실패 또는 토큰 만료";
        }
    }
}
