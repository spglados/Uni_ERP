package com.uni.uni_erp.repository.erp.sales;

import com.uni.uni_erp.domain.entity.erp.sales.SalesRefund;
import com.uni.uni_erp.dto.erp.sales.SalesRefundDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesRefundRepository extends JpaRepository<SalesRefund, Long> {

    List<SalesRefundDTO> findAllByOrderNumIn(@Param("orderNums") List<Integer> orderNums);

}
