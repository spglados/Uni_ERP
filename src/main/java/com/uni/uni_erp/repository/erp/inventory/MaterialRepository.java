package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {


    @Query("SELECT new com.uni.uni_erp.dto.erp.material.MaterialDTO(m.name, m.unit) FROM Material m WHERE m.store.id = :storeId")
    public List<MaterialDTO> findNameByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT new com.uni.uni_erp.dto.erp.material.MaterialDTO(m.id, m.name, m.unit, m.subUnit) FROM Material m WHERE m.store.id = :storeId")
    public List<MaterialDTO> findDTOByStoreId(@Param("storeId") Integer storeId);

    public List<Material> findAllByStoreId(Integer storeId);

    Material findByName(String name);
}
