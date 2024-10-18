package com.uni.uni_erp.service.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.repository.pos.PosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosService {

    private final PosRepository posRepository;

    @Autowired
    public PosService(PosRepository posRepository) {
        this.posRepository = posRepository;
    }

    public Page<Product> getProductsByStoreId(Integer storeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return posRepository.findByStoreId(storeId, pageable);
    }


}
