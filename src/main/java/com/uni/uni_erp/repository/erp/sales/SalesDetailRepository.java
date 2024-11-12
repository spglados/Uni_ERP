package com.uni.uni_erp.repository.erp.sales;

import com.uni.uni_erp.domain.entity.erp.sales.SalesDetail;
import com.uni.uni_erp.dto.erp.sales.SalesDetailDTO;
import com.uni.uni_erp.dto.erp.sales.SalesbyCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesDetailRepository extends JpaRepository<SalesDetail, Long> {
    List<SalesDetailDTO> findAllByOrderNumIn(List<Integer> orderNums);

    @Query("SELECT new com.uni.uni_erp.dto.erp.sales.SalesbyCategoryDTO(p.category, SUM(sd.unitPrice)) " +
            "FROM SalesDetail sd " +
            "JOIN Product p ON sd.itemName = p.name " +
            "GROUP BY p.category")
    List<SalesbyCategoryDTO> findItemSummaries();
}



