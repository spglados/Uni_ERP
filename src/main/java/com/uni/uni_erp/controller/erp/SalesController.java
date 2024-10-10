package com.uni.uni_erp.controller.erp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp/sales")
public class SalesController {

    @GetMapping("/record")
    public String recordPage() {
        return "erp/sales/record";
    }

    @GetMapping("/customers")
    public String customersPage() {
        return "erp/sales/customers";
    }
}
