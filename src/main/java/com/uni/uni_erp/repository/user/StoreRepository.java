package com.uni.uni_erp.repository.user;

import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.StoreDTO;
import com.uni.uni_erp.dto.sales.StoreListDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface StoreRepository extends JpaRepository<Store, Integer> {

    // Store Id 리스트
    @EntityGraph(attributePaths = {"id", "name"})
    @Query("SELECT new com.uni.uni_erp.dto.StoreDTO(s.id, s.name) FROM Store s WHERE s.user.id = :userId")
    List<StoreDTO> findStoresWithIdByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    Long countStoresCreatedLastYear(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    Long countStoresCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.uni.uni_erp.dto.sales.StoreListDTO(s.id, s.name, s.is24Hours, s.isOpen, s.createdAt, u.name) FROM Store s JOIN s.user u")
    List<StoreListDTO> findAllStoresWithUserNames();

    Optional<Store> findById(Integer id);

    // 가게 수정
    @Modifying
    @Query("UPDATE Store s SET s.name = :name, s.storeAddress = :storeAddress WHERE s.id = :id AND s.user.id = :userId")
    void updateStore(@Param("id") Integer id, @Param("name") String name, @Param("storeAddress") String storeAddress, @Param("userId") Integer userId);

    List<Store> findByUserId(Integer userId);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.user.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    void deleteByUserId(Integer userId);

    void deleteById(Integer storeId);

}
