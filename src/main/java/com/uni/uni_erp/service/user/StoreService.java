package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreDTO> ownedStores(Integer userId) {
        return storeRepository.findStoresWithIdByUserId(userId);
    }

}
