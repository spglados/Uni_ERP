package com.uni.uni_erp.repository.sales;

import com.uni.uni_erp.domain.entity.SalesDetail;
import com.uni.uni_erp.dto.sales.SalesDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesDetailRepository extends JpaRepository<SalesDetail, Long> {
    List<SalesDetailDTO> findAllByOrderNumIn(List<Integer> orderNums);
}



