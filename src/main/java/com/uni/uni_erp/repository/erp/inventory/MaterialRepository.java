package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.dto.product.MaterialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("SELECT new com.uni.uni_erp.dto.product.MaterialDTO(m.name, m.unit) FROM Material m WHERE m.store.id = :storeId")
    public List<MaterialDTO> findNameByStoreId(@Param("storeId") Integer storeId);


}
