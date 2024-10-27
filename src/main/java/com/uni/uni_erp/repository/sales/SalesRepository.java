package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

    @Query("SELECT s.orderNum FROM Sales s WHERE s.salesDate BETWEEN :startDate AND :endDate AND s.storeId = :storeId")
    List<Integer> findAllSalesNumByDateBetweenAndStoreId(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate,
                                                         @Param("storeId") Integer storeId);

    @Query(value = "SELECT MAX(s.orderNum) FROM Sales s")
    Integer findLatestOrderNum();

    @Query("SELECT SUM(a.totalPrice) FROM Sales a WHERE a.salesDate BETWEEN :startDate AND :endDate AND a.storeId = :storeId")
    Integer findSalesByDateAndStoreId(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("storeId") Integer storeId
    );

    Sales findByOrderNum(Integer orderNum);

    @Query("SELECT DISTINCT s.storeId FROM Sales s")
    List<Integer> findDistinctStoreIds();



    @Query("SELECT s.storeId AS storeId, YEAR(s.salesDate) AS year, AVG(s.totalPrice) AS averageTotalPrice " +
            "FROM Sales s " +
            "GROUP BY s.storeId, YEAR(s.salesDate)")
    List<Object[]> findYearlyAverageTotalPriceByStore();

    @Query("SELECT s.storeId AS storeId, YEAR(s.salesDate) AS year, MONTH(s.salesDate) AS month, AVG(s.totalPrice) AS averageTotalPrice " +
            "FROM Sales s " +
            "GROUP BY s.storeId, YEAR(s.salesDate), MONTH(s.salesDate)")
    List<Object[]> findMonthlyAverageTotalPriceByStoreAndYear();

    @Query("SELECT s.storeId AS storeId, YEAR(s.salesDate) AS year, MONTH(s.salesDate) AS month, DAY(s.salesDate) AS day, SUM(s.totalPrice) AS totalDailyPrice " +
            "FROM Sales s " +
            "GROUP BY s.storeId, YEAR(s.salesDate), MONTH(s.salesDate), DAY(s.salesDate)")
    List<Object[]> findDailyTotalPriceByStoreAndYearMonth();






}



