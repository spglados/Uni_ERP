package com.uni.uni_erp.repository.inventory;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
}
