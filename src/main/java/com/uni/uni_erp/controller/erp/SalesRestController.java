package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesSummaryDTO;
import com.uni.uni_erp.service.SalesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/erp/sales")
public class SalesRestController {

    @PersistenceContext
    private EntityManager entityManager;

    private final SalesService salesService;

    @GetMapping("/data")
    public List<SalesDTO> getSalesData(HttpSession session) {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startDate = today.atStartOfDay();
            LocalDateTime endDate = today.atTime(LocalTime.MAX);
            Integer storeId = (Integer) session.getAttribute("storeId");
            return salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);

        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/itemCount")
    public List<SalesDetailDTO> getItemCount(HttpSession session) {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startDate = today.atStartOfDay();
            LocalDateTime endDate = today.atTime(LocalTime.MAX);
            Integer storeId = (Integer) session.getAttribute("storeId");
            List<SalesDTO> salesList;
            salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);
            List<SalesDetailDTO> salesDetailList = salesService.findAllByOrderNumIn(salesList);
            return salesDetailList.stream()
                    .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode))
                    .entrySet().stream()
                    .map(entry -> {
                        return new SalesDetailDTO(
                                null, // id
                                entry.getKey(), // itemCode
                                entry.getValue().get(0).getItemName(), // itemName
                                entry.getValue().stream().mapToInt(SalesDetailDTO::getQuantity).sum(), // quantity
                                entry.getValue().get(0).getUnitPrice() // unitPrice
                        );
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/itemProfit")
    public List<SalesDetailDTO> getItemProfit(HttpSession session) {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startDate = today.atStartOfDay();
            LocalDateTime endDate = today.atTime(LocalTime.MAX);
            Integer storeId = (Integer) session.getAttribute("storeId");
            List<SalesDTO> salesList;
            salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);
            List<SalesDetailDTO> salesDetailList = salesService.findAllByOrderNumIn(salesList);
            return salesDetailList.stream()
                    .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode))
                    .entrySet().stream()
                    .map(entry -> {
                        return new SalesDetailDTO(
                                null, // id
                                entry.getKey(), // itemCode
                                entry.getValue().get(0).getItemName(), // itemName
                                entry.getValue().stream().mapToInt(SalesDetailDTO::getQuantity).sum(), // quantity
                                entry.getValue().get(0).getUnitPrice() // unitPrice
                        );
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/details")
    public Map<String, List<SalesSummaryDTO>> getDetails(@RequestParam(required = false) Integer year,
                                                         @RequestParam(required = false) Integer month,
                                                         HttpSession session) {
        try {
            Integer storeId = (Integer) session.getAttribute("storeId");

            LocalDate now = LocalDate.now();
            LocalDate thisMonthStart;
            LocalDate thisMonthEnd;
            LocalDate lastMonthStart;
            LocalDate lastMonthEnd;
            LocalDate thisYearStart;
            LocalDate thisYearEnd;
            LocalDate lastYearStart;
            LocalDate lastYearEnd;

            // If year and month are provided
            if (year != null && month != null) {
                thisMonthStart = LocalDate.of(year, month, 1);
                thisMonthEnd = thisMonthStart.withDayOfMonth(thisMonthStart.lengthOfMonth());

                // Calculate the last month
                LocalDate lastMonth = thisMonthStart.minusMonths(1);
                lastMonthStart = lastMonth.withDayOfMonth(1);
                lastMonthEnd = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

                // This year range
                thisYearStart = LocalDate.of(year, 1, 1);
                thisYearEnd = LocalDate.of(year, 12, 31);

                // Last year range
                lastYearStart = LocalDate.of(year - 1, 1, 1);
                lastYearEnd = LocalDate.of(year - 1, 12, 31);
            } else {
                // Default to current month if year and month are not provided
                thisMonthStart = now.withDayOfMonth(1);
                thisMonthEnd = now.withDayOfMonth(now.lengthOfMonth());

                lastMonthStart = now.minusMonths(1).withDayOfMonth(1);
                lastMonthEnd = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());

                thisYearStart = now.withDayOfYear(1);
                thisYearEnd = now.withDayOfYear(now.lengthOfYear());

                lastYearStart = now.minusYears(1).withDayOfYear(1);
                lastYearEnd = now.minusYears(1).withDayOfYear(now.minusYears(1).lengthOfYear());
            }
            // 2. Fetch sales for each period
            List<SalesDTO> thisMonthSales = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(thisMonthStart.atStartOfDay(), thisMonthEnd.atStartOfDay(), storeId);
            List<SalesDTO> thisYearSales = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(thisYearStart.atStartOfDay(), thisYearEnd.atStartOfDay(), storeId);
            List<SalesDTO> lastMonthSales = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(lastMonthStart.atStartOfDay(), lastMonthEnd.atStartOfDay(), storeId);
            List<SalesDTO> lastYearSales = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(lastYearStart.atStartOfDay(), lastYearEnd.atStartOfDay(), storeId);

            // 3. Fetch details for each period
            List<SalesDetailDTO> thisMonthDetails = salesService.findAllByOrderNumIn(thisMonthSales);
            List<SalesDetailDTO> thisYearDetails = salesService.findAllByOrderNumIn(thisYearSales);
            List<SalesDetailDTO> lastMonthDetails = salesService.findAllByOrderNumIn(lastMonthSales);
            List<SalesDetailDTO> lastYearDetails = salesService.findAllByOrderNumIn(lastYearSales);

            // 4. Group and summarize for each period
            Map<String, List<SalesSummaryDTO>> result = new HashMap<>();

            result.put("thisMonth", salesService.groupSalesDetails(thisMonthDetails));
            result.put("thisYear", salesService.groupSalesDetails(thisYearDetails));
            result.put("lastMonth", salesService.groupSalesDetails(lastMonthDetails));
            result.put("lastYear", salesService.groupSalesDetails(lastYearDetails));

            System.err.println(result);
            System.err.println(result);
            System.err.println(result);

            return result;
        } catch (Exception e) {
            log.error("Error fetching sales details", e);
            return Collections.emptyMap(); // Return empty map on error
        }
    }


}
