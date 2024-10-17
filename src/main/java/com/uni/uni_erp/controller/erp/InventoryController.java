package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.erp.product.Material;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.repository.erp.inventory.MaterialAdjustmentRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialOrderRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialRepository;
import com.uni.uni_erp.repository.erp.inventory.MaterialStatusRepository;
import com.uni.uni_erp.service.invertory.InventoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/erp/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/receiving")
    public String receivingPage() {
        return "/erp/inventory/receiving";
    }

    @GetMapping("/shipping")
    public String shippingPage() {
        return "/erp/inventory/shipping";
    }

    @GetMapping("/status")
    public String statusPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialManagementDTO> materialStatusDTOList = inventoryService.getMaterialManagementList(session);
        model.addAttribute("materialStatusList", materialStatusDTOList);
        return "/erp/inventory/status";
    }

    @GetMapping("/situation")
    public String situationPage() {
        return "/erp/inventory/situation";
    }

    @GetMapping("/day-adjustment")
    public String dayAdjustmentPage() {
        return "/erp/inventory/day-adjustment";
    }

    @GetMapping("/month-adjustment")
    public String monthAdjustmentPage() {
        return "/erp/inventory/month-adjustment";
    }

    @GetMapping("/dispose")
    public String disposePage() {
        return "/erp/inventory/dispose";
    }

}
