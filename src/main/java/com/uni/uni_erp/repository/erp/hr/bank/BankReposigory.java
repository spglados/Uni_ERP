package com.uni.uni_erp.repository.erp.hr.bank;

import com.uni.uni_erp.domain.entity.erp.hr.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankReposigory extends JpaRepository<Bank, Integer> {

        @Query("SELECT b.name FROM Bank b")
        List<String> findAllToName();

        // 모든 은행 리스트 조회
        List<Bank> findAll();
}
