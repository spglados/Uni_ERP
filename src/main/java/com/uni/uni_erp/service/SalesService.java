package com.uni.uni_erp.service;

import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.sales.SalesSummaryDTO;
import com.uni.uni_erp.dto.sales.SalesTargetDTO;
import com.uni.uni_erp.repository.sales.SalesDetailRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        List<SalesTargetDTO> salesTargetsForHour = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            LocalDateTime startDate = LocalDateTime.of(date, LocalTime.of(hour, 0));
            LocalDateTime endDate = LocalDateTime.of(date, LocalTime.of(hour + 1, 0).minusNanos(1));

            SalesDetailDTO salesDetailDTO = (SalesDetailDTO) salesRepository.findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(startDate, endDate, storeId);

            System.err.println(salesDetailDTO);
            System.err.println(salesDetailDTO);
            System.err.println(salesDetailDTO);

            SalesTargetDTO dto = new SalesTargetDTO().builder().hour(hour).salesCount(salesDetailDTO.getQuantity())..build();

            salesTargetsForHour.add(salesDetailDTO.toSalesTargetDTO());


            sales.add(salesTargetsForHour);
        }

        return sales;
    }

}
