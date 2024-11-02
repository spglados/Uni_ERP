package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.dto.erp.hr.AttendanceDTO;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.dto.sales.MostProductSaleQuantityDTO;
import com.uni.uni_erp.repository.user.UserRepository;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.invertory.InventoryService;
import com.uni.uni_erp.service.erp.hr.AttendanceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/erp")
@RequiredArgsConstructor
public class ErpController {

    private final UserRepository userRepository;
    private final AttendanceService attendanceService;
    private final InventoryService inventoryService;
    private final SalesService salesService;
    private final Gson gson;

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
        if (storeId != null) {
            model.addAttribute("storeId", storeId);
        }
        AttendanceDTO.ErpMainDTO attDTO = attendanceService.getAttendanceForMain(storeId);
        System.out.println("---------------------------------------------------");
        System.out.println(attDTO);
        System.out.println("---------------------------------------------------");
        model.addAttribute("attDTO", attDTO);

        // 오늘 날짜 재고 현황 최신화
        inventoryService.getMaterialStatus(session);

        // TODO 반드시 알람 단위가 메인 단위와 일치해야 결과가 나옴 !! 공지 필수 !!!
        // 유통기한 임박 자재
        List<MaterialDTO.nearingExpirationDateDTO> nearingExpirationDateList = inventoryService.nearingExpirationDate(session);
        // 재고 부족 알람 리스트
        List<MaterialDTO.AlarmCycleMaterialDTO> alarmCycleList = inventoryService.alarmCycle(session);
        model.addAttribute("nearingExpirationDateList", nearingExpirationDateList);
        model.addAttribute("alarmCycleList", alarmCycleList);

        // 오늘 제품 판매 수량
        List<MostProductSaleQuantityDTO> saleQuantityList = salesService.getMostSaleQuantity(session);
        model.addAttribute("saleQuantityList", saleQuantityList);

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

    @PutMapping("/store/{storeId}")
    public ResponseEntity<?> changeStoreId(@PathVariable(name = "storeId") Integer storeId, HttpSession session, Model model) {

        // 기존 storeId 변경
        session.setAttribute("storeId", storeId);
        model.addAttribute("storeId", storeId);
        return ResponseEntity.ok().build();
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
