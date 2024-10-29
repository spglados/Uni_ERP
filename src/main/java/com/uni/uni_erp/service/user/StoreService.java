package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.sales.StoreListDTO;
import com.uni.uni_erp.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreDTO> ownedStores(Integer userId) {
        return storeRepository.findStoresWithIdByUserId(userId);
    }

    public Store findById(Integer id){
        return storeRepository.findById(id).orElseThrow();
    }

    //TODO 삭제예정
    public void updateStatus(Store store) {
        storeRepository.save(store); // 변경된 payment 객체를 저장
    }

    public Long countStoresCreatedLastYear() {
        LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear() - 1, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear() - 1, 12, 31, 23, 59, 59);
        return storeRepository.countStoresCreatedLastYear(startDate, endDate);

    }

    public Long countStoresCreatedThisYear() {
        LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);
        return storeRepository.countStoresCreatedBetween(startDate, endDate);
    }

    public List<StoreListDTO> getAllStoresWithUserNames() {
        return storeRepository.findAllStoresWithUserNames();
    }


}
