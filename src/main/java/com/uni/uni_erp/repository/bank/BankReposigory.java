package com.uni.uni_erp.repository.bank;

import com.uni.uni_erp.domain.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankReposigory extends JpaRepository<Bank, Integer> {

        @Query("SELECT b.name FROM Bank b")
        List<String> findAllToName();
}
