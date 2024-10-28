package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<Integer> ownedStores(Integer userId) {
        return storeRepository.findStoresWithIdByUserId(userId);
    }

    public Store findById(Integer id){
        return storeRepository.findById(id).orElseThrow();
    }

    //TODO 삭제예정
    public void updateStatus(Store store) {

        storeRepository.save(store); // 변경된 payment 객체를 저장
    }
}
