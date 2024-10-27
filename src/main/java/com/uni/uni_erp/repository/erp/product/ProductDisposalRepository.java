package com.uni.uni_erp.repository.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.ProductDisposal;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

public interface ProductDisposalRepository extends JpaAttributeConverter<ProductDisposal, Integer> {
}
