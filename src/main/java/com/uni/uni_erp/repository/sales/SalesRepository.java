package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDataDTO;
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

    @Query("SELECT SUM(s.totalPrice) FROM Sales s WHERE s.salesDate >= :startDate AND s.salesDate <= :endDate")
    Long findTotalSalesPriceForLastYear(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(s.totalPrice) FROM Sales s WHERE s.salesDate >= :startDate AND s.salesDate <= :endDate")
    Long findTotalSalesPriceForThisYear(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);



    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesDataDTO(YEAR(s.salesDate), SUM(s.totalPrice)) " +
            "FROM Sales s " +
            "GROUP BY YEAR(s.salesDate) " +
            "ORDER BY YEAR(s.salesDate)")
    List<SalesDataDTO> findTotalPriceByYear();

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesDataDTO(MONTH(s.salesDate), SUM(s.totalPrice)) " +
            "FROM Sales s " +
            "WHERE YEAR(s.salesDate) = :year " +
            "GROUP BY MONTH(s.salesDate) " +
            "ORDER BY MONTH(s.salesDate)")
    List<SalesDataDTO> findTotalPriceByMonth(@Param("year") int year);

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesDataDTO(DAY(s.salesDate), SUM(s.totalPrice)) " +
            "FROM Sales s " +
            "WHERE MONTH(s.salesDate) = :month AND YEAR(s.salesDate) = :year " +
            "GROUP BY DAY(s.salesDate) " +
            "ORDER BY DAY(s.salesDate)")
    List<SalesDataDTO> findTotalPriceByDay(@Param("month") int month, @Param("year") int year);



}



