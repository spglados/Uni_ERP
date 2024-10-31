package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.dto.sales.*;
import com.uni.uni_erp.service.SalesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
            return salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(startDate, endDate, storeId);

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
            salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(startDate, endDate, storeId);
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
            salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(startDate, endDate, storeId);
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

    @GetMapping("/refund-data")
    public List<SalesRefundDTO> getItemRefunds(HttpSession session) {
        try {
            int year = LocalDate.now().getYear();
            LocalDateTime startDateCurrentYear = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime endDateCurrentYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);
            Integer storeId = (Integer) session.getAttribute("storeId");

            List<Integer> orderNum = salesService.findAllSalesNumByDateBetweenAndStoreId(startDateCurrentYear, endDateCurrentYear, storeId);

            List<SalesRefundDTO> refundList = salesService.findRefundByOrderNum(orderNum);

            return salesService.groupRefundDetails(refundList);
        } catch (Exception e) {
            log.error("err", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<ProductSalesDTO>> getDetails(@RequestParam Integer year,
                                                            @RequestParam Integer month,
                                                            HttpSession session) {

        // Selected Month
        LocalDateTime startDateCurrent = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateCurrent = LocalDateTime.of(year, month, startDateCurrent.getMonth().length(startDateCurrent.toLocalDate().isLeapYear()), 23, 59, 59);

        // Previous Month
        int previousMonth = month - 1;
        int previousYear = year - 1;
        if (previousMonth < 1) {
            previousMonth = 12;
        }
        LocalDateTime startDatePrevious = LocalDateTime.of(previousYear, previousMonth, 1, 0, 0);
        int lastDayOfPreviousMonth = startDatePrevious.getMonth().length(startDatePrevious.toLocalDate().isLeapYear());
        LocalDateTime endDatePrevious = LocalDateTime.of(previousYear, previousMonth, lastDayOfPreviousMonth, 23, 59, 59);

        // Current Year
        LocalDateTime startDateCurrentYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDateCurrentYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        // Last Year
        LocalDateTime startDateLastYear = LocalDateTime.of(previousYear, 1, 1, 0, 0);
        LocalDateTime endDateLastYear = LocalDateTime.of(previousYear, 12, 31, 23, 59, 59);

        Integer storeId = (Integer) session.getAttribute("storeId");

        // Fetch data for the selected month
        List<Integer> salesDataCurrentMonth = salesService.findAllSalesNumByDateBetweenAndStoreId(startDateCurrent, endDateCurrent, storeId);
        List<SalesDetailDTO> salesDetailDataCurrentMonth = salesService.findByOrderNum(salesDataCurrentMonth);
        List<SalesSummaryDTO> salesSummaryDataCurrentMonth = salesService.groupSalesDetails(salesDetailDataCurrentMonth);

        // Fetch data for the previous month
        List<Integer> salesDataPreviousMonth = salesService.findAllSalesNumByDateBetweenAndStoreId(startDatePrevious, endDatePrevious, storeId);
        List<SalesDetailDTO> salesDetailDataPreviousMonth = salesService.findByOrderNum(salesDataPreviousMonth);
        List<SalesSummaryDTO> salesSummaryDataPreviousMonth = salesService.groupSalesDetails(salesDetailDataPreviousMonth);

        // Fetch data for the selected year
        List<Integer> salesDataCurrentYear = salesService.findAllSalesNumByDateBetweenAndStoreId(startDateCurrentYear, endDateCurrentYear, storeId);
        List<SalesDetailDTO> salesDetailDataCurrentYear = salesService.findByOrderNum(salesDataCurrentYear);
        List<SalesSummaryDTO> salesSummaryDataCurrentYear = salesService.groupSalesDetails(salesDetailDataCurrentYear);

        // Fetch data for the last year
        List<Integer> salesDataLastYear = salesService.findAllSalesNumByDateBetweenAndStoreId(startDateLastYear, endDateLastYear, storeId);
        List<SalesDetailDTO> salesDetailDataLastYear = salesService.findByOrderNum(salesDataLastYear);
        List<SalesSummaryDTO> salesSummaryDataLastYear = salesService.groupSalesDetails(salesDetailDataLastYear);


        List<ProductSalesDTO> productSalesList = new ArrayList<>();

        for (SalesSummaryDTO currentMonthData : salesSummaryDataCurrentMonth) {
            // Get last month's sales for the same product
            Integer lastMonthSales = salesSummaryDataPreviousMonth.stream()
                    .filter(prevMonthData -> prevMonthData.getItemName().equals(currentMonthData.getItemName()))
                    .map(SalesSummaryDTO::getTotalQuantity)
                    .findFirst()
                    .orElse(0); // Default to 0 if not found
            // Get current year's sales for the same product
            Integer yearlySales = salesSummaryDataCurrentYear.stream()
                    .filter(currentYearData -> currentYearData.getItemName().equals(currentMonthData.getItemName()))
                    .map(SalesSummaryDTO::getTotalQuantity)
                    .reduce(0, Integer::sum); // Sum total for current year
            // Get last year's sales for the same product
            Integer lastYearSales = salesSummaryDataLastYear.stream()
                    .filter(lastYearData -> lastYearData.getItemName().equals(currentMonthData.getItemName()))
                    .map(SalesSummaryDTO::getTotalQuantity)
                    .reduce(0, Integer::sum); // Sum total for last year
            // Calculate monthly growth rate
            Double monthlyGrowthRate = (lastMonthSales > 0) ?
                    Double.parseDouble(String.format("%.1f", ((double) (currentMonthData.getTotalQuantity() - lastMonthSales) * 100) / lastMonthSales)) :
                    null;
            // Calculate yearly growth rate
            Double yearlyGrowthRate = (lastYearSales > 0) ?
                    Double.parseDouble(String.format("%.1f", ((double) (yearlySales - lastYearSales) * 100 / lastYearSales))) :
                    null;
            // Create and add the ProductSalesDTO to the list
            ProductSalesDTO productSalesDTO = ProductSalesDTO.builder()
                    .productName(currentMonthData.getItemName())
                    .monthlySales(currentMonthData.getTotalQuantity()) // Current month's total quantity
                    .lastMonthSales(lastMonthSales) // Previous month's total quantity
                    .monthlyGrowthRate(monthlyGrowthRate) // Monthly growth rate calculation
                    .yearlySales(yearlySales) // Current year's total quantity
                    .lastYearSales(lastYearSales) // Last year's total quantity
                    .yearlyGrowthRate(yearlyGrowthRate) // Yearly growth rate calculation
                    .profit((currentMonthData.getTotalQuantity() * currentMonthData.getUnitPrice())) // Example profit calculation
                    .build();
            productSalesList.add(productSalesDTO);
        }

        return ResponseEntity.ok(productSalesList);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(HttpSession session) {

        session.getAttribute("storeId");

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate lastYear = LocalDate.now().minusYears(1);

        List<SalesTargetDTO> todaySalesTargets = salesService.getSalesTargetByHour(1, today);
        List<SalesTargetDTO> yesterdaySalesTargets = salesService.getSalesTargetByHour(1, yesterday);
        List<SalesTargetDTO> lastYearSalesTargets = salesService.getSalesTargetByHour(1, lastYear);

        List<SalesComparisonDTO> salesComparison = salesService.getSalesComparison(todaySalesTargets, yesterdaySalesTargets, lastYearSalesTargets);

        Gson gson = new Gson();
        String salesComparisonJson = gson.toJson(salesComparison);
        return ResponseEntity.ok(salesComparisonJson);
    }


}