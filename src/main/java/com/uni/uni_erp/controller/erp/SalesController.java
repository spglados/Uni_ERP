package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.service.SalesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/erp/sales")
public class SalesController {

    @PersistenceContext
    private EntityManager entityManager;

    private final SalesService salesService;

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
//        model.addAttribute("salesHistory", salesService.findByUserId(user.getId()));

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

    @GetMapping("/data")
    @ResponseBody
    public List<SalesDTO> getSalesData(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                       @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate) {
        try {
            List<SalesDTO> salesList = salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            return salesList;
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/itemCount")
    @ResponseBody
    public List<SalesDetailDTO> getItemCount(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                             @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate) {
        try {
            List<SalesDTO> salesList = salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            List<SalesDetailDTO> salesDetailList = salesService.findAllByOrderNumIn(salesList);

            System.err.println(salesDetailList);

            return salesDetailList;
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/itemProfit")
    @ResponseBody
    public List<SalesDetailDTO> getItemProfit(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                           @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate) {
        try {
            List<SalesDTO> salesList = salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            List<SalesDetailDTO> salesDetailList = salesService.findAllByOrderNumIn(salesList);

            System.err.println(salesDetailList);

            return salesDetailList;
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

}
