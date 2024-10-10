package com.uni.uni_erp.service;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.repository.SalesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;

    @Transactional
    public void save(Sales sales) {
        salesRepository.save(sales);
    }

    @Transactional
    public void update(Integer id, Sales sales) {
        Sales updateSales = new Sales().builder()
                .id(id)
                .user(sales.getUser())
                .itemName(sales.getItemName())
                .itemCode(sales.getItemCode())
                .specs(sales.getSpecs())
                .quantity(sales.getQuantity())
                .unitPrice(sales.getUnitPrice())
                .tax(sales.getTax())
                .totalPrice(sales.getTotalPrice())
                .attachmentUri(sales.getAttachmentUri())
                .salesDate(sales.getSalesDate())
                .status(sales.getStatus())
                .build();

        salesRepository.save(updateSales);
    }

    public List<Sales> findByUserId(Integer userId) {
        return salesRepository.findByUser_Id(userId);
    }

    @Transactional
    public void delete(Integer id) {
        salesRepository.deleteById(id);
    }

}
