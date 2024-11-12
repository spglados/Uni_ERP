package com.uni.uni_erp.repository.admin;

import com.uni.uni_erp.domain.entity.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a.password FROM Admin a WHERE a.username = :username")
    String findPasswordByUsername(String username);

    Admin findByUsername(String username);
}
