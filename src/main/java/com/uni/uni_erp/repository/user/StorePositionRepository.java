package com.uni.uni_erp.repository.user;

import com.uni.uni_erp.domain.entity.erp.hr.EmpPosition;
import com.uni.uni_erp.dto.store.StorePositionDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StorePositionRepository extends JpaRepository<EmpPosition, Integer> {


    // 특정 스토어의 포지션 목록 조회
    @EntityGraph(attributePaths = {"store"})
    @Query("SELECT new com.uni.uni_erp.dto.store.StorePositionDTO(ep.id, ep.name, ep.minRequiredNum, ep.store.id) " +
            "FROM EmpPosition ep WHERE ep.store.id = :storeId")
    List<StorePositionDTO> findByStoreId(@Param("storeId") Integer storeId);


    // 포지션 ID로 조회 (DTO 사용)
    @Query("SELECT new com.uni.uni_erp.dto.store.StorePositionDTO(ep.id, ep.name, ep.minRequiredNum, ep.store.id) " +
            "FROM EmpPosition ep WHERE ep.id = :id")
    Optional<StorePositionDTO> findPositionById(@Param("id") Integer id);


    // 포지션 수정
    @Modifying
    @Query("UPDATE EmpPosition ep SET ep.name = :name, ep.minRequiredNum = :minRequiredNum WHERE ep.id = :id")
    void updatePosition(@Param("id") Integer id, @Param("name") String name, @Param("minRequiredNum") Integer minRequiredNum);

    // 포지션 삭제
    @Modifying
    @Query("DELETE FROM EmpPosition ep WHERE ep.id = :id")
    void deletePosition(@Param("id") Integer id);

    @Query("SELECT ep FROM EmpPosition ep WHERE ep.name = :name AND ep.store.id = :storeId")
    Optional<EmpPosition> findByNameAndStoreId(@Param("name") String name, @Param("storeId") Integer storeId);


    @Query("SELECT ep FROM EmpPosition ep WHERE ep.id = :id AND ep.store.id = :storeId")
    Optional<EmpPosition> findByIdAndStoreId(Integer id, Integer storeId);

    @Query("SELECT ep FROM EmpPosition ep WHERE ep.name = :name AND ep.store.id = :storeId")
    Optional<EmpPosition> findByName(String name, Integer storeId);
}


