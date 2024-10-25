package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.dto.erp.material.MaterialDTO;
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
    public String receivingPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialOrderDTO> materialOrderDTOList = inventoryService.getMaterialOrder(session);
        model.addAttribute("materialOrderList", materialOrderDTOList);
        return "/erp/inventory/receiving";
    }

    @GetMapping("/registration")
    public String registerPage() {
        return "/erp/inventory/register";
    }

    @GetMapping("/status")
    public String statusPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialManagementDTO> materialManagementDTOList = inventoryService.getMaterialManagementList(session);
        model.addAttribute("materialManagementList", materialManagementDTOList);
        return "/erp/inventory/status";
    }

    @GetMapping("/situation")
    public String situationPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialStatusDTO> materialStatusDTOList = inventoryService.getMaterialStatus(session);
        model.addAttribute("materialStatusList", materialStatusDTOList);
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

    @GetMapping("/disposal")
    public String disposePage() {
        return "/erp/inventory/dispose";
    }

}
