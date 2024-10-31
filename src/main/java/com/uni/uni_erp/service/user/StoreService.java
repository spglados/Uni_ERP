package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.dto.sales.StoreListDTO;
import com.uni.uni_erp.dto.store.StorePositionDTO;
import com.uni.uni_erp.dto.store.StoreSaveDTO;
import com.uni.uni_erp.dto.store.StoreUpdateDTO;
import com.uni.uni_erp.repository.user.StorePositionRepository;
import com.uni.uni_erp.repository.user.StoreRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StorePositionRepository storePositionRepository;
    private final UserRepository userRepository;

    // 특정 사용자가 소유한 스토어 목록 조회
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
    public List<StorePositionDTO> getPositionsByStoreId(Integer storeId) {
        return storePositionRepository.findByStoreId(storeId)
                .stream()
                .map(position -> new StorePositionDTO(position.getId(), position.getName(), position.getMinRequiredNum(), storeId)) // storeId를 사용
                .collect(Collectors.toList());
    }

    // 포지션 수정
    @Transactional
    public void updatePosition(Integer id, StorePositionDTO storePositionDTO) {
        storePositionRepository.updatePosition(id, storePositionDTO.getName(), storePositionDTO.getMinRequiredNum());
    }

    // 포지션 삭제
    @Transactional
    public void deletePosition(Integer id) {
        storePositionRepository.deletePosition(id);
    }

    //포지션 등록
    @Transactional
    public void createPosition(StorePositionDTO storePositionDTO) {
        EmpPosition empPosition = EmpPosition.builder()
                .name(storePositionDTO.getName())
                .minRequiredNum(storePositionDTO.getMinRequiredNum())
                .store(storeRepository.findById(storePositionDTO.getStoreId())
                        .orElseThrow(() -> new RuntimeException("스토어를 찾을 수 없습니다."))) // 스토어 ID로 스토어 조회
                .build();

        storePositionRepository.save(empPosition);
    }

    // 포지션 ID로 조회
    public StorePositionDTO getPositionById(Integer id) {
        EmpPosition position = storePositionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 포지션을 찾을 수 없습니다."));

        return new StorePositionDTO(position.getId(), position.getName(), position.getMinRequiredNum(), position.getStore() != null ? position.getStore().getId() : null);
    }

    // 특정 ID로 가게 정보를 조회하는 메서드
    public StoreUpdateDTO getStoreById(Integer id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다. ID: " + id));
        return new StoreUpdateDTO(store.getId(), store.getName(), store.getStoreAddress()); // StoreUpdateDTO로 변환하여 반환
    }

    // 가게 정보 수정
    @Transactional
    public void updateStore(Integer id, StoreUpdateDTO storeUpdateDTO) {
        // ID로 가게를 조회
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다. ID: " + id));

        // 수정할 필드 업데이트
        store.setName(storeUpdateDTO.getName());
        store.setStoreAddress(storeUpdateDTO.getStoreAddress());

        // 변경된 가게 정보를 저장
        storeRepository.save(store);
    }

    @Transactional
    public Store registerStore(StoreSaveDTO storeSaveDTO) {
        User user = userRepository.findById(storeSaveDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Store store = storeSaveDTO.toStore(user);

        return storeRepository.save(store);
    }

    public List<Store> findAllById(Integer userid){
        return storeRepository.findByUserId(userid);
    }

    public Integer getStoreCountByUserId(Integer userId) {
        return storeRepository.countByUserId(userId);
    }
}
