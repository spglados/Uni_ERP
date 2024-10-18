package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
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
import java.util.List;
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
            System.err.println(storeId);
            System.err.println(startDate);
            System.err.println(endDate);
            System.err.println(salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId));
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

}
