package com.uni.uni_erp.repository.user;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.StoreDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    // Store Id 리스트
    @EntityGraph(attributePaths = {"id", "name"})
    @Query("SELECT new com.uni.uni_erp.dto.StoreDTO(s.id, s.name) FROM Store s WHERE s.user.id = :userId")
    List<StoreDTO> findStoresWithIdByUserId(@Param("userId") Integer userId);

    Optional<Store> findById(Integer id);

    // 가게 수정
    @Modifying
    @Query("UPDATE Store s SET s.name = :name, s.storeAddress = :storeAddress WHERE s.id = :id AND s.user.id = :userId")
    void updateStore(@Param("id") Integer id, @Param("name") String name, @Param("storeAddress") String storeAddress, @Param("userId") Integer userId);

}
