package com.uni.uni_erp.repository.common;

import com.uni.uni_erp.domain.entity.common.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Integer> {

    Response findByContactId(Integer contactId);

}
