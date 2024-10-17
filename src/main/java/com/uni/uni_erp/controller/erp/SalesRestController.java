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
    private final StoreService storeService;

    @GetMapping("/data")
    public List<SalesDTO> getSalesData(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                       @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate,
                                       @RequestParam(value = "storeId", required = false) Integer storeId) {
        try {
            if (storeId != null) {
                return salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), storeId);
            } else {
                return salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            }
        } catch (Exception e) {
            log.error("Error searching sales records by date", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/itemCount")
    public List<SalesDetailDTO> getItemCount(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                             @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate,
                                             @RequestParam(value = "storeId", required = false) Integer storeId) {
        try {
            List<SalesDTO> salesList;
            if (storeId != null) {
                salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), storeId);
            } else {
                salesList = salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            }
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
    public List<SalesDetailDTO> getItemProfit(@RequestParam(value = "startDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1).truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String startDate,
                                              @RequestParam(value = "endDate", required = true, defaultValue = "#{T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()}") String endDate,
                                              @RequestParam(value = "storeId", required = false) Integer storeId) {
        try {
            List<SalesDTO> salesList;
            if (storeId != null) {
                salesList = salesService.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), storeId);
            } else {
                salesList = salesService.findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
            }
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
