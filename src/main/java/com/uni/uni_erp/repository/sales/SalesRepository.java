package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.sales.SalesDTO;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate);

    List<SalesDTO> findAllBySalesDateBetweenAndStoreIdOrderBySalesDateAsc(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

}



