package com.uni.uni_erp.controller.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.repository.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp")
public class ErpController {

    private final UserRepository userRepository;

    public ErpController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 메인페이지 요청
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Map<String, Object>> test = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "장건우");

        Map<String, Object> map2 = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "장건우시발");
        Map<String, Object> map3 = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "장건우시발");
        Map<String, Object> map4 = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "장건우시발");
        Map<String, Object> map5 = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "장건우시발");
        Map<String, Object> map6 = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "장건우시발");
        test.add(map);
        test.add(map2);
        test.add(map3);
        test.add(map4);
        test.add(map5);
        test.add(map6);
        try {
            model.addAttribute("test", new ObjectMapper().writeValueAsString(test));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "erp/main";
    }

    // 모든 사용자 데이터를 반환하는 REST 엔드포인트
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<?> getUsers() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "장건우");
        return ResponseEntity.ok(map);
    }

}
