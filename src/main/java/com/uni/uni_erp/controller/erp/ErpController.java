package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.dto.erp.hr.AttendanceDTO;
import com.uni.uni_erp.repository.user.UserRepository;
import com.uni.uni_erp.service.erp.hr.AttendanceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp")
@RequiredArgsConstructor
public class ErpController {

    private final UserRepository userRepository;
    private final AttendanceService attendanceService;

    /**
     * 메인페이지 요청
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {

        List<StoreDTO> storeList = (List<StoreDTO>) session.getAttribute("storeList");
        if(storeList != null) {
            model.addAttribute("storeList", storeList);
        }
        Integer storeId = (Integer) session.getAttribute("storeId");
        AttendanceDTO.ErpMainDTO attDTO = attendanceService.getAttendanceForMain(storeId);
        System.out.println("---------------------------------------------------");
        System.out.println(attDTO);
        System.out.println("---------------------------------------------------");
        model.addAttribute("attDTO", attDTO);
        return "erp/main";
    }

    /**
     * 가게선택 페이지
     *
     * @return
     */
    @GetMapping("/storeChoice")
    public String storeChoicePage(Model model) {

        return "erp/storeSelect";
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
