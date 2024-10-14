package com.uni.uni_erp.repository;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.SalesDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<SalesDTO> findAllBySalesDateBetweenOrderBySalesDateDesc(LocalDateTime startDate, LocalDateTime endDate);

}



