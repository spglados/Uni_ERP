package com.uni.uni_erp.repository.common;

import com.uni.uni_erp.domain.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("SELECT c FROM Contact c WHERE c.status = 'OPEN'")
    Page<Contact> findAllByStatus(Pageable pageable);
}
