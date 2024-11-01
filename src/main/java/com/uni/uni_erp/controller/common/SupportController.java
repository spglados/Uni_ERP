package com.uni.uni_erp.controller.common;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.ContactDTO;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.service.common.SupportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/contact")
    public ResponseEntity<?> contact(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("userSession");
        ContactDTO dto = ContactDTO.builder()
                .userId(user.getId())
                .title(title)
                .content(content)
                .build();
        supportService.save(dto);
        return ResponseEntity.ok("작성 완료");
    }

}
