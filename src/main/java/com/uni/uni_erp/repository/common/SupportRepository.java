package com.uni.uni_erp.repository.common;

import com.uni.uni_erp.domain.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<Contact, Integer> {

}
