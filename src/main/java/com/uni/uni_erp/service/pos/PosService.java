package com.uni.uni_erp.service.pos;

import com.uni.uni_erp.domain.entity.erp.pos.Pos;
import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.repository.pos.PosRepository;
import com.uni.uni_erp.repository.pos.PosRepository2;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PosService {

    private final PosRepository posRepository;
    private final PosRepository2 posRepository2;

//    public Page<Product> getProductsByStoreId(Integer storeId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return posRepository.findByStoreId(storeId, pageable);
//    }

    public Page<Product> getProductsByStoreIdAndCategory(Integer storeId, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return posRepository.findByStoreIdAndCategory(storeId, category, pageable);
    }

    public Pos getPosDetail(int id){
        return posRepository2.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("POS not found for id: " + id));
    }

    @Transactional
    public void withdrawAmount(Integer posId, Long withdrawalAmount) {
        posRepository2.withdrawAmount(posId, withdrawalAmount);
    }

    @Transactional
    public void addAmount(Integer posId, Long depositAmount) {
        posRepository2.addAmount(posId, depositAmount);
    }


}
