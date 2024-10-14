package com.uni.uni_erp.service.invertory;

import com.uni.uni_erp.repository.inventory.MaterialOrderRepository;
import com.uni.uni_erp.repository.inventory.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final MaterialRepository materialRepository;
    private final MaterialOrderRepository materialOrderRepository;

}
