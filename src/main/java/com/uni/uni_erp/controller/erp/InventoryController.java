package com.uni.uni_erp.controller.erp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp/inventory")
public class InventoryController {

    @GetMapping("/receiving")
    public String receivingPage() {
        return "/erp/inventory/receiving";
    }

    @GetMapping("/shipping")
    public String shippingPage() {
        return "/erp/inventory/shipping";
    }

    @GetMapping("/status")
    public String statusPage() {
        return "/erp/inventory/status";
    }

    @GetMapping("/adjustment")
    public String adjustmentPage() {
        return "/erp/inventory/adjustment";
    }

    @GetMapping("/suppliers")
    public String suppliersPage() {
        return "/erp/inventory/suppliers";
    }

}
