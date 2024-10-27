package com.uni.uni_erp.controller.admin;

import com.uni.uni_erp.domain.entity.Sales;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.repository.erp.product.ProductRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import com.uni.uni_erp.service.payment.SalesService;
import com.uni.uni_erp.service.product.ProductService;
import com.uni.uni_erp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final SalesService salesService;


    // 관리자 Home 페이지
    @GetMapping("/main")
    public String mainPage(Model model) {

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        // 구독자 수
        Integer subscribeUserCount = userService.getPremiumUserCount();
        // 작년 구독자 수
        Integer subscribeUserCountForLastYear =  userService.getPremiumUserCountForLastYear();
        // 목표치 구독자수 130%
        // int percentOfSubscribeUserCount = (int) (subscribeUserCountForLastYear * 1.3);
        //TODO 임시값 서비스호출필요
        int percentOfSubscribeUserCount = 100;

        double percentageOfSubscribeUser = (subscribeUserCount != null && percentOfSubscribeUserCount != 0)
                ? (subscribeUserCount / (double) percentOfSubscribeUserCount) * 100
                : 0;
        model.addAttribute("subscribeUserCount",subscribeUserCount);
        model.addAttribute("percentOfSubscribeUserCount", percentOfSubscribeUserCount);
        model.addAttribute("percentageOfSubscribeUser", (int) Math.round(percentageOfSubscribeUser));

        // 매출 상승률
        //TODO 임시값 서비스호출필요
        Integer salesAmount = 123456;
        // 작년 매출
        // TODO 임시값 서비스호출필요
        Integer salesAmountForLastYear = 123456;
        // 목표치 매출 105%
        int percentOfSalesAmountForLastYear = (int) (salesAmountForLastYear * 1.05);
        //int percentOfSalesAmountForLastYear = ;

        double percentageOfSalesAmount = (salesAmount != null && percentOfSalesAmountForLastYear != 0)
                ? (salesAmount / (double) percentOfSalesAmountForLastYear) * 100
                : 0;
        model.addAttribute("salesAmount",salesAmount);
        model.addAttribute("percentOfSalesAmountForLastYear", percentOfSalesAmountForLastYear);
        model.addAttribute("percentageOfSalesAmount", (int) Math.round(percentageOfSalesAmount));

        // 가게 수
        //TODO 임시값 서비스호출필요
        Integer storeCount = 700;
        // 작년 가게수
        // TODO 임시값 서비스호출필요
        Integer storeCountForLastYear = 500;
        // 목표치 가게수 110%
        int percentOfStoreCountForLastYear = (int) (storeCountForLastYear * 1.1);
        //int percentOfSalesAmountForLastYear = ;

        double percentageOfStoreCount = (storeCount != null && percentOfStoreCountForLastYear != 0)
                ? (storeCount / (double) percentOfStoreCountForLastYear) * 100
                : 0;
        model.addAttribute("storeCount",storeCount);
        model.addAttribute("percentOfStoreCountForLastYear", percentOfStoreCountForLastYear);
        model.addAttribute("percentageOfStoreCount", (int) Math.round(percentageOfStoreCount));

        // 상품 카테고리별 매출
        // 상품 카테고리 추출
        List<String> productList = productService.getDistinctCategories();
        model.addAttribute("productList",productList);
        // 상품 카테고리 매출 추출
        //TODO 임시값
        List<Integer> salesAmounts = Arrays.asList(10000, 50000, 106000, 15000);
        int totalSales = salesAmounts.stream().mapToInt(Integer::intValue).sum();

        // 비율 계산
        List<Integer> percentageSaleList = new ArrayList<>();
        for (Integer amount : salesAmounts) {
            int percentage = (int) ((amount / (double) totalSales) * 100); // int로 변환
            percentageSaleList.add(percentage);
        }
        model.addAttribute("percentageSaleList",percentageSaleList);

        // 가게별 매출

        /*model.addAttribute("averageSalesData", averageSalesData);
        model.addAttribute("storeId", storeId);
        model.addAttribute("year", year);*/

        return "admin/dashboard";
    }





}
