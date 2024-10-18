package com.uni.uni_erp.service;

import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import com.uni.uni_erp.repository.sales.SalesDetailRepository;
import com.uni.uni_erp.repository.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
//    @Transactional
//    public void save(Sales sales) {
//        salesRepository.save(sales);
//    }
//
//    @Transactional
//    public void update(Integer id, Sales sales) {
//        Sales updateSales = new Sales().builder()
//                .id(id)
//                .user(sales.getUser())
//                .itemName(sales.getItemName())
//                .itemCode(sales.getItemCode())
//                .specs(sales.getSpecs())
//                .quantity(sales.getQuantity())
//                .unitPrice(sales.getUnitPrice())
//                .tax(sales.getTax())
//                .totalPrice(sales.getTotalPrice())
//                .attachmentUri(sales.getAttachmentUri())
//                .salesDate(sales.getSalesDate())
//                .status(sales.getStatus())
//                .build();
//
//        salesRepository.save(updateSales);
//    }
//
//    public List<Sales> findByUserId(Integer userId) {
//        return salesRepository.findByUser_Id(userId);
//    }
//
//    @Transactional
//    public void delete(Integer id) {
//        salesRepository.deleteById(id);
//    }

}
