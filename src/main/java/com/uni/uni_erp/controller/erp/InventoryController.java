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
