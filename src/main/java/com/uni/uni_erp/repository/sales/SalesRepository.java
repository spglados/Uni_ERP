package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesQuantityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesQuantityDTO(sd.itemCode,sd.quantity,s.salesDate) " +
            "FROM Sales s JOIN SalesDetail sd ON s.orderNum = sd.orderNum " +
            "WHERE s.salesDate BETWEEN :startDate AND :endDate " +
            "AND sd.status IN :statuses " +
            "AND s.storeId = :storeId")
    List<SalesQuantityDTO> findSalesQuantity(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("storeId") Integer storeId,
                                             @Param("statuses") List<SalesDetail.SaleStatus> statuses);


    @Query("SELECT s.orderNum FROM Sales s WHERE s.salesDate BETWEEN :startDate AND :endDate AND s.storeId = :storeId")
    List<Integer> findAllSalesNumByDateBetweenAndStoreId(@Param("startDate") LocalDateTime startDate,
                                                                         @Param("endDate") LocalDateTime endDate,
                                                                         @Param("storeId") Integer storeId);

    @Query(value = "SELECT MAX(s.orderNum) FROM Sales s")
    Integer findLatestOrderNum();
}



