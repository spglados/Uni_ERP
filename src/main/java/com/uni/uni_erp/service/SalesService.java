package com.uni.uni_erp.service;

import com.uni.uni_erp.dto.sales.*;
import com.uni.uni_erp.repository.sales.SalesDetailRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final SalesDetailRepository salesDetailRepository;

    public List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId) {
        return salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);
    }

    public List<SalesDetailDTO> findAllByOrderNumIn(List<SalesDTO> salesDTO) {
        List<Integer> orderNums = salesDTO.stream()
                .map(SalesDTO::getOrderNum)
                .toList();

        return salesDetailRepository.findAllByOrderNumIn(orderNums);
    }

    public List<SalesDetailDTO> findByOrderNum(List<Integer> orderNum) {
        return salesDetailRepository.findAllByOrderNumIn(orderNum);
    }


    public List<SalesSummaryDTO> groupSalesDetails(List<SalesDetailDTO> salesDetailList) {
        return salesDetailList.stream()
                .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode))
                .entrySet().stream()
                .map(entry -> {
                    List<SalesDetailDTO> itemList = entry.getValue();
                    int totalQuantity = itemList.stream().mapToInt(SalesDetailDTO::getQuantity).sum();
                    return new SalesSummaryDTO(
                            itemList.get(0).getItemName(), // Item name from the first item in the group
                            totalQuantity,                 // Total quantity sold
                            itemList.get(0).getUnitPrice() // Original unit price
                    );
                })
                .toList();
    }

    public List<SalesTargetDTO> getSalesTargetByHour(Integer storeId, LocalDate date) {
        List<SalesTargetDTO> sales = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            LocalDateTime startDate = LocalDateTime.of(date, LocalTime.of(hour, 0));
            LocalDateTime endDate = (hour == 23)
                    ? LocalDateTime.of(date, LocalTime.of(23, 59, 59, 999999999)).minusNanos(1)
                    : LocalDateTime.of(date, LocalTime.of(hour + 1, 0)).minusNanos(1);

            List<SalesDTO> currentSales = salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);
            int totalSales = currentSales.stream().mapToInt(SalesDTO::getTotalPrice).sum();
            int salesCount = currentSales.size();

            LocalDateTime lastMonthStartDate = startDate.minusMonths(1);
            LocalDateTime lastMonthEndDate = lastMonthStartDate.plusMonths(1).minusDays(1);
            List<SalesDTO> lastMonthSales = salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(lastMonthStartDate, lastMonthEndDate, storeId);

            // Calculate average sales of last month for this hour
            int lastMonthTotalSales = lastMonthSales.stream().mapToInt(SalesDTO::getTotalPrice).sum();
            int avgLastMonthSales = !lastMonthSales.isEmpty() ? lastMonthTotalSales / lastMonthSales.size() : 0;

            // Apply a 5% growth factor to set the target profit
            double growthFactor = 1.05;
            int targetProfit = (int) (avgLastMonthSales * growthFactor);

            // Create a SalesTargetDTO object
            SalesTargetDTO salesTargetDTO = SalesTargetDTO.builder()
                    .hour(hour)
                    .targetProfit(targetProfit)
                    .sales(totalSales)
                    .salesCount(salesCount)
                    .build();

            sales.add(salesTargetDTO);
        }
        return sales;
    }

    public List<SalesComparisonDTO> getSalesComparison(List<SalesTargetDTO> todaySales,
                                                       List<SalesTargetDTO> yesterdaySales,
                                                       List<SalesTargetDTO> lastYearSales) {

        List<SalesComparisonDTO> salesComparison = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            // Get data for the current hour
            SalesTargetDTO today = todaySales.get(hour);
            SalesTargetDTO yesterday = yesterdaySales.get(hour);
            SalesTargetDTO lastYear = lastYearSales.get(hour);

            // Calculate percentage differences
            Double percentageComparedToYesterday = calculatePercentageDifference(today.getSales(), yesterday.getSales());
            Double percentageComparedToLastYear = calculatePercentageDifference(today.getSales(), lastYear.getSales());

            // Calculate differences in total sales
            Integer salesComparedToLastDay = today.getSales() - yesterday.getSales();
            Integer salesComparedToLastYear = today.getSales() - lastYear.getSales();

            // Build the SalesComparisonDTO for this hour
            SalesComparisonDTO comparisonDTO = SalesComparisonDTO.builder()
                    .hour(hour)
                    .lastDayTargetProfit(yesterday.getTargetProfit())
                    .lastDayTotalSales(yesterday.getSales())
                    .lastDaySalesCount(yesterday.getSalesCount())
                    .todayTargetProfit(today.getTargetProfit())
                    .todayTotalSales(today.getSales())
                    .todaySalesCount(today.getSalesCount())
                    .salesComparedToLastDay(salesComparedToLastDay)
                    .percentageComparedToLastDay(percentageComparedToYesterday)
                    .salesComparedToLastYear(salesComparedToLastYear)
                    .percentageComparedToLastYear(percentageComparedToLastYear)
                    .build();

            // Add the comparison DTO to the list
            salesComparison.add(comparisonDTO);
        }

        return salesComparison;
    }

    private Double calculatePercentageDifference(int todaySales, int comparisonSales) {
        // Return null if either todaySales or comparisonSales is 0
        if (todaySales == 0 || comparisonSales == 0) {
            return (double) 0;
        }

        // Calculate the percentage difference
        double difference = ((double) (todaySales - comparisonSales) / comparisonSales) * 100;

        // Round to one decimal place
        BigDecimal roundedDifference = BigDecimal.valueOf(difference)
                .setScale(1, RoundingMode.HALF_UP); // Rounds to one decimal place

        return roundedDifference.doubleValue();
    }

    public List<Integer> findAllSalesNumByDateBetweenAndStoreId(LocalDateTime startDate, LocalDateTime endDate, Integer storeId) {
        return salesRepository.findAllSalesNumByDateBetweenAndStoreId(startDate, endDate, storeId);
    }

}
