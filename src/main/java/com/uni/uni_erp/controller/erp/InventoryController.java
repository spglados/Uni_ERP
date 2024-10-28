package com.uni.uni_erp.controller.erp;

import com.google.gson.Gson;
import com.uni.uni_erp.dto.erp.material.MaterialDTO;
import com.uni.uni_erp.service.invertory.InventoryService;
import com.uni.uni_erp.util.Str.UnitCategory;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/erp/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final Gson gson;

    @GetMapping("/receiving")
    public String receivingPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialOrderDTO> materialOrderDTOList = inventoryService.getMaterialOrder(session);
        List<MaterialDTO> materialDTOList = inventoryService.getMaterialListForOrder(session);
        model.addAttribute("materialDTOList", materialDTOList);
        model.addAttribute("materialDTOListJson", gson.toJson(materialDTOList));
        model.addAttribute("materialOrderList", materialOrderDTOList);
        return "/erp/inventory/receiving";
    }

    @PostMapping("/receiving")
    public String saveReceiving(HttpSession session, @ModelAttribute MaterialDTO.MaterialOrderDTO materialOrderDTO) {
        inventoryService.saveMaterialOrder(session, materialOrderDTO);
        return "/erp/inventory/receiving";
    }

    @GetMapping("/registration")
    public String registerPage(Model model, HttpSession session) {
        List<UnitCategory> unitCategories = Arrays.stream(UnitCategory.values()).toList();
        model.addAttribute("unitCategories", unitCategories);

        return "/erp/inventory/register";
    }

    @PostMapping("/registration")
    public ResponseEntity<MaterialDTO.MaterialSaveDTO> registerPage(@RequestBody MaterialDTO.MaterialSaveDTO materialSaveDTO, HttpSession session) {
        return ResponseEntity.ok(inventoryService.saveMaterial(materialSaveDTO, session));
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

    @GetMapping("/correction")
    public String correctionPage(Model model) {
        List<UnitCategory> unitCategories = Arrays.stream(UnitCategory.values()).toList();
        model.addAttribute("unitCategories", unitCategories);
        return "/erp/inventory/correction";
    }

    @GetMapping("/materials/{materialCode}")
    @ResponseBody
    public ResponseEntity<MaterialDTO.MaterialSaveDTO> getMaterial(@PathVariable Long materialCode) {
        MaterialDTO.MaterialSaveDTO material = inventoryService.getMaterialByCode(materialCode);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(material);
    }

    @PutMapping("/materials")
    @ResponseBody
    public ResponseEntity<String> updateMaterial(
             @RequestBody MaterialDTO.MaterialSaveDTO materialSaveDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        boolean isUpdated = inventoryService.updateMaterial(materialSaveDTO);
        if (isUpdated) {
            return ResponseEntity.ok("자재가 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(500).body("자재 수정에 실패했습니다.");
        }
    }

    @GetMapping("/day-adjustment")
    public String dayAdjustmentPage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialStatusDTO> materialStatusDTOList = inventoryService.getMaterialStatus(session);
        model.addAttribute("materialStatusList", materialStatusDTOList);
        return "/erp/inventory/day-adjustment";
    }

    @PostMapping("/day-adjustment")
    public ResponseEntity<Void> saveDayAdjustment(HttpSession session, @RequestBody List<MaterialDTO.MaterialDayAdjustmentDTO> reqDtoList) {

        inventoryService.saveDayAdjustmentList(session, reqDtoList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/month-adjustment")
    public String monthAdjustmentPage() {
        return "/erp/inventory/month-adjustment";
    }

    @PostMapping("/month-adjustment")
    public ResponseEntity<List<MaterialDTO.DisposalHistoryDTO>> monthAdjustmentPage(HttpSession session) {
        List<MaterialDTO.DisposalHistoryDTO> disposalHistory = inventoryService.getDisposalHistory(session);
        System.out.println(disposalHistory.toString());
        return ResponseEntity.ok(disposalHistory);
    }

    @GetMapping("/disposal")
    public String disposePage(Model model, HttpSession session) {
        List<MaterialDTO.MaterialDisposalListDTO> materialDisposalListDTO = inventoryService.getMaterialDisposalList(session);
        List<MaterialDTO.ProductDisposalListDTO> productDisposalListDTO = inventoryService.getProductDisposalList(session);
        model.addAttribute("materialDisposalList", materialDisposalListDTO);
        model.addAttribute("productDisposalList", productDisposalListDTO);
        return "/erp/inventory/dispose";
    }

    @PostMapping("/disposal")
    public ResponseEntity<Void> saveDisposal(HttpSession session, @RequestBody MaterialDTO.DisposalSaveDTO reqDtoList) {

        inventoryService.saveDisposal(session, reqDtoList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/disposalHistory")
    public String disposeHistoryPage(Model model, HttpSession session) {
        return "/erp/inventory/disposalHistory";
    }

}
