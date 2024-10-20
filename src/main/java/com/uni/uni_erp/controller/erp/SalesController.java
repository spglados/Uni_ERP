package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

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

    @GetMapping("/record")
    public String recordPage() {
        return "erp/sales/record";
    }

    @GetMapping("/history")
    public String salesHistory(Model model, @SessionAttribute("userSession") User user) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        return "erp/sales/history";
    }

    @GetMapping("/statistics")
    public String salesStatistics() {

        return "erp/sales/statistics";
    }

}
