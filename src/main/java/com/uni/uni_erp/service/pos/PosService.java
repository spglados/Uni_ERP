package com.uni.uni_erp.service.pos;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.repository.pos.PosRepository;
import com.uni.uni_erp.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosService {

    private final PosRepository posRepository;

    @Autowired
    public PosService(PosRepository posRepository) {
        this.posRepository = posRepository;
    }

    public List<Product> getProductsByStoreId(Integer storeId) {
        return posRepository.findByStoreId(storeId);
    }


}
