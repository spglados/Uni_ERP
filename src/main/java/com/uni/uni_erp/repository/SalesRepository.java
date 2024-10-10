package com.uni.uni_erp.repository;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.dto.SalesDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    public List<Sales> findByUser_Id(Integer userId);


}



