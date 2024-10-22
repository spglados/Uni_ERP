package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.sales.SalesComparisonDTO;
import com.uni.uni_erp.dto.sales.SalesTargetDTO;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/erp/sales")
public class SalesController {

    private final SalesService salesService;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @GetMapping("/record")
    public String recordPage() {
        return "erp/sales/record";
    }

    @GetMapping("/history")
    public String salesHistory(Model model, @SessionAttribute("userSession") User user) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        return "erp/sales/history";
    }

    @GetMapping("/statistics")
    public String salesStatistics(Model model, HttpSession session) {

        session.getAttribute("storeId");

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate lastYear = LocalDate.now().minusYears(1);

        List<SalesTargetDTO> todaySalesTargets = salesService.getSalesTargetByHour(1, today);
        List<SalesTargetDTO> yesterdaySalesTargets = salesService.getSalesTargetByHour(1, yesterday);
        List<SalesTargetDTO> lastYearSalesTargets = salesService.getSalesTargetByHour(1, lastYear);

        List<SalesComparisonDTO> salesComparison = salesService.getSalesComparison(todaySalesTargets, yesterdaySalesTargets, lastYearSalesTargets);

        model.addAttribute("salesComparison", salesComparison);

        return "erp/sales/statistics";
    }

}
