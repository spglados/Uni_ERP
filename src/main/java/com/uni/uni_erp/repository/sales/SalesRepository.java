package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.dto.sales.SalesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

    @Query("SELECT s FROM Sales s JOIN FETCH SalesDetail sd ")
    List<SalesDTO.SalesQuantityDTO> findSalesProductBySalesDateAndStoreId(LocalDateTime today, Integer storeId);

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesDTO.SalesQuantityDTO(s.orderNum, sd.itemCode, sd.itemName, sd.quantity, sd.status, s.salesDate) " +
            "FROM Sales s JOIN SalesDetail sd " +
            "WHERE s.salesDate BETWEEN :startDate AND :endDate " +
            "AND sd.status IN (SalesDetail.SaleStatus.결제, SalesDetail.SaleStatus.환불) " +
            "AND s.storeId = :storeId")
    List<SalesDTO.SalesQuantityDTO> findSalesQuantity(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("storeId") Integer storeId);

}



