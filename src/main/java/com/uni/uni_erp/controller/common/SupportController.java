package com.uni.uni_erp.controller.common;

import com.uni.uni_erp.dto.ContactDTO;
import com.uni.uni_erp.service.common.SupportService;
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
    public ResponseEntity<?> contact(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("tel") String tel, @RequestParam("content") String content) {

        ContactDTO dto = ContactDTO.builder().name(name).email(email).tel(tel).content(content).build();

        supportService.save(dto);

        return ResponseEntity.ok("작성 완료");
    }

}
