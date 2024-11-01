package com.uni.uni_erp.service;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.domain.entity.SalesRefund;
import com.uni.uni_erp.dto.CostPerEmployeeDTO;
import com.uni.uni_erp.dto.sales.*;
import com.uni.uni_erp.exception.errors.Exception401;
import com.uni.uni_erp.repository.erp.hr.AttendanceRepository;
import com.uni.uni_erp.repository.sales.SalesDetailRepository;
import com.uni.uni_erp.repository.sales.SalesRefundRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final SalesDetailRepository salesDetailRepository;
    private final SalesRefundRepository salesRefundRepository;
    private final AttendanceRepository attendanceRepository;

    public List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId) {
        return salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(startDate, endDate, storeId);
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

    // 아이템 코드로 그룹화
    public List<SalesSummaryDTO> groupSalesDetails(List<SalesDetailDTO> salesDetailList) {
        return salesDetailList.stream()
                .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode))
                .entrySet().stream()
                .map(entry -> {
                    List<SalesDetailDTO> itemList = entry.getValue();
                    int totalQuantity = itemList.stream().mapToInt(SalesDetailDTO::getQuantity).sum();
                    return new SalesSummaryDTO(
                            itemList.get(0).getItemName(),
                            totalQuantity,
                            itemList.get(0).getUnitPrice()
                    );
                })
                .toList();
    }


    public List<SalesRefundDTO> groupRefundDetails(List<SalesRefundDTO> salesRefundList) {
        return salesRefundList.stream()
                .collect(Collectors.groupingBy(SalesRefundDTO::getItemCode))
                .entrySet().stream()
                .map(entry -> {
                    List<SalesRefundDTO> itemList = entry.getValue();
                    int totalQuantity = itemList.stream().mapToInt(SalesRefundDTO::getQuantity).sum();
                    return new SalesRefundDTO(
                            entry.getKey(), // Item code from the group key
                            itemList.get(0).getItemName(), // Item name from the first item in the group
                            totalQuantity,  // Total quantity sold
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

            List<SalesDTO> currentSales = salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(startDate, endDate, storeId);
            int totalSales = currentSales.stream().mapToInt(SalesDTO::getTotalPrice).sum();
            int salesCount = currentSales.size();

            LocalDateTime lastMonthStartDate = startDate.minusMonths(1);
            LocalDateTime lastMonthEndDate = lastMonthStartDate.plusMonths(1).minusDays(1);
            List<SalesDTO> lastMonthSales = salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(lastMonthStartDate, lastMonthEndDate, storeId);

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

    private Sales createSales(SalesInsertDTO salesInsertDTO) {
        return Sales.builder()
                .orderNum(salesInsertDTO.getOrderNum())
                .totalPrice(salesInsertDTO.getTotalPrice())
                .salesDate(salesInsertDTO.getSalesDate())
                .storeId(salesInsertDTO.getStoreId())
                .build();
    }

    @Transactional
    public void saveSales(SalesInsertDTO salesInsertDTO) {
        Sales sales = createSales(salesInsertDTO);
        salesRepository.save(sales);
    }

    @Transactional
    public void saveSalesDetail(SalesDetailDTO salesDetailDTO, Integer orderNum) {
        Sales sales = salesRepository.findByOrderNum(orderNum);
        SalesDetail salesDetail = SalesDetail.builder()
                .itemCode(salesDetailDTO.getItemCode())
                .itemName(salesDetailDTO.getItemName())
                .quantity(salesDetailDTO.getQuantity())
                .unitPrice(salesDetailDTO.getUnitPrice())
                .sales(sales)
                .build();
        salesDetailRepository.save(salesDetail);
    }

    @Transactional
    public void saveSalesRefund(SalesRefundInsertDTO salesRefundDTO, Integer orderNum) {
        Sales sales = salesRepository.findByOrderNum(orderNum);
        SalesRefund salesRefund = SalesRefund.builder()
                .itemCode(salesRefundDTO.getItemCode())
                .itemName(salesRefundDTO.getItemName())
                .quantity(salesRefundDTO.getQuantity())
                .unitPrice(salesRefundDTO.getUnitPrice())
                .refundStatus(SalesRefund.RefundStatus.취소)
                .sales(sales)
                .build();

        salesRefundRepository.save(salesRefund);
    }

    public Integer findLatestOrderNum() {
        Integer latestOrderNum = salesRepository.findLatestOrderNum();
        if (latestOrderNum == null) {
            return 1000000;
        } else {
            return latestOrderNum;
        }
    }

    public List<CostPerEmployeeDTO> calculateEmployeeSales(LocalDateTime startDate, LocalDateTime endDate, Integer storeId) {
        // Split the time range into 10-minute intervals
        List<LocalDateTime> timeIntervals = splitIntoIntervals(startDate, endDate, 30);

        // Create a map to store total sales and order counts per employee
        Map<Integer, CostPerEmployeeDTO> employeeSalesMap = new HashMap<>();

        // Loop through each 10-minute interval
        for (int i = 0; i < timeIntervals.size() - 1; i++) {
            LocalDateTime intervalStart = timeIntervals.get(i);
            LocalDateTime intervalEnd = timeIntervals.get(i + 1);
            Timestamp startTimestamp = Timestamp.valueOf(intervalStart.atZone(ZoneId.systemDefault()).toLocalDateTime());
            Timestamp endTimestamp = Timestamp.valueOf(intervalEnd.atZone(ZoneId.systemDefault()).toLocalDateTime());

            // Fetch employees working in this 10-minute interval
            List<Integer> currentWorkingEmployees = attendanceRepository.findAttendanceByDateAndStoreId(startTimestamp, endTimestamp, storeId);

            // Calculate sales for this 10-minute interval
            Integer salesForInterval = salesRepository.findSalesByDateAndStoreId(intervalStart, intervalEnd, storeId);

            if (salesForInterval != null && !currentWorkingEmployees.isEmpty()) {
                // Divide sales among employees who were working during this interval
                Integer salesPerEmployee = salesForInterval / currentWorkingEmployees.size();

                for (Integer employee : currentWorkingEmployees) {
                    // Update or create CostPerEmployeeDTO for each working employee
                    employeeSalesMap.computeIfAbsent(employee, id ->
                            CostPerEmployeeDTO.builder()
                                    .id(id)
                                    .costPer(0)
                                    .totalOrders(0)
                                    .build()
                    );

                    // Update the DTO with new sales and order counts
                    CostPerEmployeeDTO dto = employeeSalesMap.get(employee);
                    dto.setCostPer(dto.getCostPer() + salesPerEmployee);
                    dto.setTotalOrders(dto.getTotalOrders() + 1);
                }
            }
        }

        return new ArrayList<>(employeeSalesMap.values());
    }

    private List<LocalDateTime> splitIntoIntervals(LocalDateTime startDate, LocalDateTime endDate, int intervalMinutes) {
        List<LocalDateTime> intervals = new ArrayList<>();
        LocalDateTime current = startDate;
        while (current.isBefore(endDate)) {
            intervals.add(current);
            current = current.plusMinutes(intervalMinutes);
        }
        intervals.add(endDate); // Include the end time
        return intervals;
    }

    public List<Integer> findAllSalesNumByDateBetweenAndStoreId(LocalDateTime startDateCurrent, LocalDateTime endDateCurrent, Integer storeId) {
        return salesRepository.findAllSalesNumByDateBetweenAndStoreId(startDateCurrent, endDateCurrent, storeId);
    }

    public List<SalesDetailDTO> compareQuantities(List<SalesDetailDTO> salesDetails1, List<SalesDetailDTO> salesDetails2) {
        Map<Long, Integer> quantities1 = salesDetails1.stream()
                .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode, Collectors.summingInt(SalesDetailDTO::getQuantity)));
        Map<Long, Integer> quantities2 = salesDetails2.stream()
                .collect(Collectors.groupingBy(SalesDetailDTO::getItemCode, Collectors.summingInt(SalesDetailDTO::getQuantity)));

        List<SalesDetailDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : quantities1.entrySet()) {
            Long itemCode = entry.getKey();
            Integer quantity1 = entry.getValue();
            Integer quantity2 = quantities2.get(itemCode);

            if (quantity2 != null && !quantity1.equals(quantity2)) {
                result.add(SalesDetailDTO.builder()
                        .itemCode(itemCode)
                        .itemName(salesDetails1.stream()
                                .filter(d -> d.getItemCode().equals(itemCode))
                                .findFirst()
                                .orElseThrow()
                                .getItemName())
                        .quantity(quantity1 - quantity2)
                        .unitPrice(salesDetails1.stream()
                                .filter(d -> d.getItemCode().equals(itemCode))
                                .findFirst()
                                .orElseThrow()
                                .getUnitPrice())
                        .build());
            }
        }

        return result;
    }

    public List<RevenuePerDTO> employeeRevenueSquash(List<CostPerEmployeeDTO> costPerEmployeeThisYear, List<CostPerEmployeeDTO> costPerEmployeeThisMonth, List<CostPerEmployeeDTO> costPerEmployeeThisWeek) {
        List<RevenuePerDTO> employeeSalesData = new ArrayList<>();
        Set<String> employeeNames = new HashSet<>();
        employeeNames.addAll(costPerEmployeeThisWeek.stream().map(CostPerEmployeeDTO::getName).toList());
        employeeNames.addAll(costPerEmployeeThisMonth.stream().map(CostPerEmployeeDTO::getName).toList());
        employeeNames.addAll(costPerEmployeeThisYear.stream().map(CostPerEmployeeDTO::getName).toList());

        for (String employeeName : employeeNames) {
            RevenuePerDTO data = new RevenuePerDTO();
            data.setName(employeeName);

            CostPerEmployeeDTO weekly = costPerEmployeeThisWeek.stream().filter(e -> e != null && e.getName() != null && e.getName().equals(employeeName)).findFirst().orElse(null);
            CostPerEmployeeDTO monthly = costPerEmployeeThisMonth.stream().filter(e -> e != null && e.getName() != null && e.getName().equals(employeeName)).findFirst().orElse(null);
            CostPerEmployeeDTO yearly = costPerEmployeeThisYear.stream().filter(e -> e != null && e.getName() != null && e.getName().equals(employeeName)).findFirst().orElse(null);
            if (weekly != null) {
                data.setWeeklyCostPer(weekly.getCostPer());
                data.setWeeklyTotalOrders(weekly.getTotalOrders());
            }
            if (monthly != null) {
                data.setMonthlyCostPer(monthly.getCostPer());
                data.setMonthlyTotalOrders(monthly.getTotalOrders());
            }
            if (yearly != null) {
                data.setYearlyCostPer(yearly.getCostPer());
                data.setYearlyTotalOrders(yearly.getTotalOrders());
            }
            // Check if the employee has any sales data
            if (data.getWeeklyCostPer() != null || data.getMonthlyCostPer() != null || data.getYearlyCostPer() != null) {
                employeeSalesData.add(data);
            }
        }
        return employeeSalesData;
    }

    public List<SalesRefundDTO> findRefundByOrderNum(List<Integer> orderNum) {
        return salesRefundRepository.findAllByOrderNumIn(orderNum);
    }

    public List<SalesbyCategoryDTO> getItemSummaries() {
        return salesDetailRepository.findItemSummaries();
    }

    public Long getTotalSalesForLastYear() {
        LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear() - 1, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear() - 1, 12, 31, 23, 59, 59);
        return salesRepository.findTotalSalesPriceForLastYear(startDate, endDate);
    }

    public Long getTotalSalesForThisYear() {
        LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);
        return salesRepository.findTotalSalesPriceForThisYear(startDate, endDate);
    }


    // 연도별
    public List<SalesDataDTO> getTotalPriceByYear() {
        return salesRepository.findTotalPriceByYear();
    }

    // 월별
    public List<SalesDataDTO> getTotalSalesForCurrentYearByMonth() {
        int currentYear = LocalDateTime.now().getYear();
        return salesRepository.findTotalPriceByMonth(currentYear);
    }
    // 일별
    public List<SalesDataDTO> getTotalSalesForCurrentMonth() {
        // 현재 날짜 가져오기
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // 해당 월의 매출 데이터 조회
        return salesRepository.findTotalPriceByDay(currentMonth, currentYear);
    }

    public List<MostProductSaleQuantityDTO> getMostSaleQuantity(HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if(storeId == null) {
            throw new Exception401("가게 정보가 없거나, 인증이 유효하지 않습니다.");
        }

        return salesRepository.findMostProductSaleQuantityByStoreId(storeId, LocalDate.now());

    }
}
