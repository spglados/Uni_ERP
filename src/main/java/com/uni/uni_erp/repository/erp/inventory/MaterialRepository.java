package com.uni.uni_erp.repository.erp.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Integer> {


    @Query("SELECT new com.uni.uni_erp.dto.erp.material.MaterialDTO(m.name, m.unit, m.subUnit) FROM Material m WHERE m.store.id = :storeId")
    public List<MaterialDTO> findNameByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT new com.uni.uni_erp.dto.erp.material.MaterialDTO(m.id, m.name, m.unit, m.subUnit) FROM Material m WHERE m.store.id = :storeId")
    List<MaterialDTO> findDTOByStoreId(@Param("storeId") Integer storeId);

    List<Material> findAllByStoreId(Integer storeId);

    @Query("SELECT m FROM Material m WHERE m.materialCode IN :materialCodes AND m.store.id = :storeId")
    List<Material> findAllByStoreIdForMaterialCode(Integer storeId, List<Long> materialCodes);

    Material findByName(String name);

    List<Material> findByStoreId(Integer storeId);

    Optional<Material> findByMaterialCode(Long materialCode);

    Optional<Material> findByIdAndStoreId(Integer materialId, Integer storeId);

    @Query("SELECT m FROM Material m WHERE m.materialCode IN :materialCodeList")
    List<Material> findByMaterialCodes(List<Long> materialCodeList);

}
