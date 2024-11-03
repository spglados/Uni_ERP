package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateDesc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesQuantityDTO(sd.itemCode,sd.quantity,s.salesDate) " +
            "FROM Sales s JOIN SalesDetail sd ON s.orderNum = sd.orderNum " +
            "WHERE s.salesDate BETWEEN :startDate AND :endDate " +
            "AND s.storeId = :storeId")
    List<SalesQuantityDTO> findSalesQuantity(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("storeId") Integer storeId);


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

    @Query("SELECT new com.uni.uni_erp.dto.sales.MostProductSaleQuantityDTO(sd.itemCode, sd.itemName, SUM(sd.quantity)) FROM Sales s JOIN FETCH SalesDetail sd ON s.orderNum = sd.orderNum WHERE s.storeId = :storeId AND FUNCTION('DATE', s.salesDate) = :today GROUP BY sd.itemCode ORDER BY SUM(sd.quantity) DESC limit 5")
    List<MostProductSaleQuantityDTO> findMostProductSaleQuantityByStoreId(Integer storeId, LocalDate today);

    @Query("SELECT new com.uni.uni_erp.dto.sales.SalesInfoDTO(" +
            "FUNCTION('HOUR', s.salesDate), " +
            "SUM(s.totalPrice)" +
            ") " +
            "FROM Sales s " +
            "WHERE s.salesDate >= :todayStart AND s.salesDate < :todayEnd " +
            "AND s.storeId = :storeId " +
            "GROUP BY FUNCTION('HOUR', s.salesDate) " +
            "ORDER BY SUM(s.totalPrice) DESC " +
            "LIMIT 4")
    List<SalesInfoDTO> findTop4MostSalesByToday(@Param("todayStart") LocalDateTime todayStart,
                                                @Param("todayEnd") LocalDateTime todayEnd, Integer storeId);
}



