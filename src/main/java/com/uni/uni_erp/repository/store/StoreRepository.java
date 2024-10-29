package com.uni.uni_erp.repository.store;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.StoreDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    // Store Id 리스트
    @EntityGraph(attributePaths = {"id", "name"})
    @Query("SELECT new com.uni.uni_erp.dto.StoreDTO(s.id, s.name) FROM Store s WHERE s.user.id = :userId")
    List<StoreDTO> findStoresWithIdByUserId(@Param("userId") Integer userId);

}
