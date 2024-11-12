package com.uni.uni_erp.repository.payment;

import com.uni.uni_erp.domain.entity.user.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface smsrepository extends JpaRepository<Sms, Integer> {
}
