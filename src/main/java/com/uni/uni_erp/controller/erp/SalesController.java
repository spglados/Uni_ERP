package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.CostPerEmployeeDTO;
import com.uni.uni_erp.dto.erp.hr.EmployeeDTO;
import com.uni.uni_erp.dto.sales.RevenuePerDTO;
import com.uni.uni_erp.dto.sales.SalesComparisonDTO;
import com.uni.uni_erp.dto.sales.SalesTargetDTO;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.erp.hr.HrService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.*;
import java.util.Calendar;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/erp/sales")
public class SalesController {

    private final SalesService salesService;
    private final HrService hrService;

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
    public String salesStatistics() {

        return "erp/sales/statistics";
    }

    @GetMapping("/revenue-per-employee")
    public String revenuePerEmployee(Model model) {
        LocalDate today = LocalDate.now();
        YearMonth thisMonth = YearMonth.from(today);
        Year thisYear = Year.from(today);


        List<CostPerEmployeeDTO> costPerEmployeeThisWeek = salesService.calculateEmployeeSales(today.with(DayOfWeek.MONDAY).minusDays(today.getDayOfWeek().getValue() - 1).atStartOfDay(), today.with(DayOfWeek.MONDAY).plusDays(6).atStartOfDay(), 1);
        for (CostPerEmployeeDTO costPerEmployeeDTO : costPerEmployeeThisWeek) {
            EmployeeDTO employee = hrService.getEmployeeById(costPerEmployeeDTO.getId());
            costPerEmployeeDTO.setName(employee.getName());
        }

        List<CostPerEmployeeDTO> costPerEmployeeThisMonth = salesService.calculateEmployeeSales(thisMonth.atDay(1).atStartOfDay(), thisMonth.atEndOfMonth().atTime(LocalTime.MAX), 1);
        for (CostPerEmployeeDTO costPerEmployeeDTO : costPerEmployeeThisWeek) {
            EmployeeDTO employee = hrService.getEmployeeById(costPerEmployeeDTO.getId());
            costPerEmployeeDTO.setName(employee.getName());
        }

        List<CostPerEmployeeDTO> costPerEmployeeThisYear = salesService.calculateEmployeeSales(thisYear.atDay(1).atStartOfDay(), thisYear.atDay(thisYear.length()).atTime(LocalTime.MAX), 1);
        for (CostPerEmployeeDTO costPerEmployeeDTO : costPerEmployeeThisWeek) {
            EmployeeDTO employee = hrService.getEmployeeById(costPerEmployeeDTO.getId());
            costPerEmployeeDTO.setName(employee.getName());
        }

        List<RevenuePerDTO> employeeSalesData = salesService.employeeRevenueSquash(costPerEmployeeThisYear, costPerEmployeeThisMonth, costPerEmployeeThisWeek);

        model.addAttribute("employeeSalesData", employeeSalesData);

        return "erp/sales/revenuePer";
    }

    @GetMapping("/refunds")
    public String refunds() {
        return "erp/sales/refunds";
    }

}
