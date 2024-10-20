package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/erp/sales")
public class SalesController {

    @PersistenceContext
    private EntityManager entityManager;

    private final SalesService salesService;
    private final StoreService storeService;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    // 1. 판매 등록 페이지
    @GetMapping("/record")
    public String recordPage() {
        return "erp/sales/record";
    }

    @PostMapping("/record/create")
    public String createRecord(@ModelAttribute SalesDTO salesDTO, RedirectAttributes redirectAttributes) {
        try {
//            salesService.save(salesDTO.toSalesEntity());
            redirectAttributes.addFlashAttribute(SUCCESS, "정상 등록 완료");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "등록 실패");
            log.error("Error creating sales record", e);
        }
        return "redirect:/erp/sales/record";
    }

    // 2. 조회 + 관리/수정 페이지
    @GetMapping("/history")
    public String salesHistory(Model model, @SessionAttribute("userSession") User user) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("currentYear", currentYear);

        return "erp/sales/history";
    }

    @PostMapping("/history/update/{id}")
    public String updateSalesRecord(@ModelAttribute SalesDTO salesDTO, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
//            salesDTO.setId(id);
//            salesService.update(id, salesDTO.toSalesEntity());
            redirectAttributes.addFlashAttribute(SUCCESS, "판매 기록이 성공적으로 업데이트되었습니다!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "판매 기록 업데이트 실패.");
        }
        return "redirect:/erp/sales/history";
    }


    @PostMapping("/history/delete/{id}")
    public String deleteSalesRecord(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
//            salesService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS, "판매 기록이 성공적으로 삭제되었습니다!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "판매 기록 삭제 실패.");
        }
        return "redirect:/erp/sales/history";
    }

    // 3. 차트 분석/통계 페이지
    @GetMapping("/statistics")
    public String salesStatistics() {

        return "erp/sales/statistics";
    }

}
