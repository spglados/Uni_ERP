package com.uni.uni_erp.service.refund;

import com.uni.uni_erp.domain.entity.payment.Refund;
import com.uni.uni_erp.repository.refund.RefundRepository;
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
