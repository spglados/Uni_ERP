package com.uni.uni_erp.controller.admin;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.sales.SalesDataDTO;
import com.uni.uni_erp.dto.sales.SalesbyCategoryDTO;
import com.uni.uni_erp.dto.sales.StoreListDTO;
import com.uni.uni_erp.service.product.ProductService;
import com.uni.uni_erp.service.sales.SalesService;
import com.uni.uni_erp.service.user.StoreService;
import com.uni.uni_erp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final SalesService salesService;
    private final StoreService storeService;




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
        Long salesAmount = salesService.getTotalSalesForThisYear();
        // 작년 매출
        Long salesAmountForLastYear = salesService.getTotalSalesForLastYear();
        // 목표치 매출 105%
        int percentOfSalesAmountForLastYear = (int) (salesAmountForLastYear * 1.05);

        double percentageOfSalesAmount = (salesAmount != null && percentOfSalesAmountForLastYear != 0)
                ? (salesAmount / (double) percentOfSalesAmountForLastYear) * 100
                : 0;
        model.addAttribute("salesAmount",salesAmount);
        model.addAttribute("percentOfSalesAmountForLastYear", percentOfSalesAmountForLastYear);
        model.addAttribute("percentageOfSalesAmount", (int) Math.round(percentageOfSalesAmount));

        // 가게 수
        Long storeCount = storeService.countStoresCreatedThisYear();
        // 작년 가게수
        Long storeCountForLastYear = storeService.countStoresCreatedLastYear();
        // 목표치 가게수 110%
        int percentOfStoreCountForLastYear = (int) (storeCountForLastYear * 1.1);

        double percentageOfStoreCount = (storeCount != null && percentOfStoreCountForLastYear != 0)
                ? (storeCount / (double) percentOfStoreCountForLastYear) * 100
                : 0;
        model.addAttribute("storeCount",storeCount);
        model.addAttribute("percentOfStoreCountForLastYear", percentOfStoreCountForLastYear);
        model.addAttribute("percentageOfStoreCount", (int) Math.round(percentageOfStoreCount));

        // 상품 카테고리별 매출
        List<SalesbyCategoryDTO> salesbyCategory = salesService.getItemSummaries();
        List<String> productList = new ArrayList<>();

        // 상품 카테고리 추출
        // 상품 카테고리 매출 추출
        List<Long> sumAmount = new ArrayList<>();
        for (SalesbyCategoryDTO dto : salesbyCategory) {
            productList.add(dto.getCategory());
            sumAmount.add(dto.getTotalUnitPrice());
        }
        model.addAttribute("productList",productList);

        // 비율 계산
        Long totalSales = sumAmount.stream().mapToLong(Long::longValue).sum();
        List<Integer> percentageSaleList = new ArrayList<>();
        for (Long amount : sumAmount) {
            int percentage = (int) ((amount / (double) totalSales) * 100); // int로 변환
            percentageSaleList.add(percentage);
        }
        model.addAttribute("percentageSaleList",percentageSaleList);

        // 가게별 매출
        //연도별
        List<SalesDataDTO> salesYearData = salesService.getTotalPriceByYear();
        List<Integer> salesYear = new ArrayList<>();
        List<Long> salesYearTotalPrice = new ArrayList<>();

        for (SalesDataDTO data : salesYearData) {
            salesYear.add(data.getDate());
            salesYearTotalPrice.add(data.getTotalPrice());
        }

        model.addAttribute("salesYear",salesYear);
        model.addAttribute("salesYearTotalPrice",salesYearTotalPrice);

        // 달별
        List<SalesDataDTO> salesMonthData = salesService.getTotalSalesForCurrentYearByMonth();
        List<Integer> salesMonth = new ArrayList<>();
        List<Long> salesMonthTotalPrice = new ArrayList<>();
        for (SalesDataDTO data : salesMonthData) {
            salesMonth.add(data.getDate());
            salesMonthTotalPrice.add(data.getTotalPrice());
        }
        model.addAttribute("salesMonth",salesMonth);
        model.addAttribute("salesMonthTotalPrice",salesMonthTotalPrice);

        // 일별
        // 현재 월의 매출 데이터 가져오기
        List<SalesDataDTO> salesDailyData = salesService.getTotalSalesForCurrentMonth();

        // 모델에 데이터 추가
        List<Integer> salesDays = new ArrayList<>();
        List<Long> salesTotalPrice = new ArrayList<>();
        for (SalesDataDTO data : salesDailyData) {
            salesDays.add(data.getDate()); // SalesDataDTO에서 Day를 가져온다고 가정
            salesTotalPrice.add(data.getTotalPrice());
        }
        model.addAttribute("salesDays", salesDays);
        model.addAttribute("salesTotalPrice", salesTotalPrice);


        /*model.addAttribute("averageSalesData", averageSalesData);
        model.addAttribute("storeId", storeId);
        model.addAttribute("year", year);*/

        return "admin/dashboard";
    }


    @GetMapping("/userManagement")
    public String userManagementPage(Model model){
        List<User> userList = userService.findAll();
        model.addAttribute("userList",userList);
        return "/admin/userManagement";
    }

    @GetMapping("/storeManagement")
    public String storeManagementPage(Model model){
        List<StoreListDTO> storeList = storeService.getAllStoresWithUserNames();
        model.addAttribute("storeList",storeList);

        return "/admin/storeManagement";
    }

    @GetMapping("/store/details/{id}")
    public String getStoreDetails(@PathVariable("id") Integer id, Model model) {
        // StoreService를 통해 가게 정보를 조회
        Store store = storeService.findById(id);



        if (store == null) {
            // 가게가 존재하지 않을 경우 404 페이지로 리다이렉트하거나 에러 처리
            return "error/404"; // 예시: 에러 페이지 경로
        }

        model.addAttribute("store", store);
        return "/admin/storeDetails"; // 뷰의 이름 (storeDetails.jsp)
    }

    @GetMapping("/salesManagement")
    public String salesManagementPage(Model model){
        List<StoreListDTO> storeList = storeService.getAllStoresWithUserNames(); // 가게 목록 가져오기
        model.addAttribute("storeList", storeList);
        return "/admin/salesManagement";
    }







}
