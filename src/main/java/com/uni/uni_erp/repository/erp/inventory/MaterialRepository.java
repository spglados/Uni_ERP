package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("SELECT m.name FROM Material m WHERE m.store.id = :storeId")
    public List<String> findNameByStoreId(Integer storeId);

}
