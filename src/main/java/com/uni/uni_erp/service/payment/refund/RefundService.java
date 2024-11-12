package com.uni.uni_erp.service.payment.refund;

import com.uni.uni_erp.domain.entity.payment.Refund;
import com.uni.uni_erp.repository.erp.refund.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;

    public List<Refund> getRefundById(Integer id) {
        return refundRepository.findByUserId(id);
    }
}
