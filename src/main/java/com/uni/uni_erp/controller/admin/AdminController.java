package com.uni.uni_erp.controller.admin;

import com.uni.uni_erp.domain.entity.Contact;
import com.uni.uni_erp.domain.entity.Notice;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.AdminDTO;
import com.uni.uni_erp.dto.ContactDTO;
import com.uni.uni_erp.dto.NoticeDTO;
import com.uni.uni_erp.dto.ResponseDTO;
import com.uni.uni_erp.dto.sales.SalesDataDTO;
import com.uni.uni_erp.dto.sales.StoreListDTO;
import com.uni.uni_erp.service.AdminService;
import com.uni.uni_erp.service.SalesService;
import com.uni.uni_erp.service.common.ContactService;
import com.uni.uni_erp.service.common.NoticeService;
import com.uni.uni_erp.service.common.ResponseService;
import com.uni.uni_erp.service.payment.PaymentService;
import com.uni.uni_erp.service.product.ProductService;
import com.uni.uni_erp.service.user.StoreService;
import com.uni.uni_erp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final SalesService salesService;
    private final StoreService storeService;
    private final AdminService adminService;
    private final NoticeService noticeService;
    private final ResponseService responseService;
    private final ContactService contactService;
    private final PaymentService paymentService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody AdminDTO.LoginDTO dto, HttpSession session) {
        AdminDTO admin = adminService.login(dto);
        session.setAttribute("adminSession", admin);
        if (admin != null) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // 관리자 Home 페이지
    @GetMapping("/main")
    public String mainPage(Model model) {

        // 총 구독 이익
        Long totalProfit = paymentService.findAllSumAmount();
        // 총 환불액
        Long totalRefund = paymentService.findAllSumRefund();
        // 총 이익
        Long totalNetProfit = totalProfit - totalRefund;
        NumberFormat formatter = new DecimalFormat("#,###");
        String netProfitFormatted = formatter.format(totalNetProfit / 10000);

        // 회원수
        Long totalUserCount = userService.countUsers();
        // 구독자 수
        Integer subscribeUserCount = userService.getPremiumUserCount();
        // 전체 유저 / 구독자 비올
        double subscriptionRate = ((double) subscribeUserCount / totalUserCount) * 100;
        double subscriptionRateDouble = Double.parseDouble(String.format("%.2f", subscriptionRate));
        // 작년 구독자 수
        Integer subscribeUserCountForLastYear = userService.getPremiumUserCountForLastYear();
        // 목표치 구독자수 130%
        int percentOfSubscribeUserCount = (int) (subscribeUserCountForLastYear * 1.3);

        double percentageOfSubscribeUser = percentOfSubscribeUserCount != 0
                ? subscribeUserCount / (double) percentOfSubscribeUserCount * 100
                : 0;
        model.addAttribute("totalUserCount", totalUserCount);
        model.addAttribute("subscriptionRateDouble", subscriptionRateDouble);
        model.addAttribute("netProfitFormatted", netProfitFormatted);
        model.addAttribute("subscribeUserCount", subscribeUserCount);
        model.addAttribute("percentOfSubscribeUserCount", percentOfSubscribeUserCount);
        model.addAttribute("percentageOfSubscribeUser", (int) Math.round(percentageOfSubscribeUser));

        // 매출 평균
        Long salesAmount = salesService.getTotalSalesForThisYear();
        Long storeCount = storeService.countStoresCreatedThisYear();
        salesAmount = salesAmount / storeCount;
        // 작년 매출
        Long salesAmountForLastYear = salesService.getTotalSalesForLastYear();
        Long storeCountForLastYear = storeService.countStoresCreatedLastYear();
        salesAmountForLastYear = salesAmountForLastYear / storeCountForLastYear;
        // 목표치 매출 105%
        long percentOfSalesAmountForLastYear = (long) (salesAmountForLastYear * 1.05);

        double percentageOfSalesAmount = percentOfSalesAmountForLastYear != 0
                ? salesAmount / (double) percentOfSalesAmountForLastYear * 100
                : 0;
        model.addAttribute("salesAmount", salesAmount);
        model.addAttribute("percentOfSalesAmountForLastYear", percentOfSalesAmountForLastYear);
        model.addAttribute("percentageOfSalesAmount", (int) Math.round(percentageOfSalesAmount));

        // 목표치 가게수 110%
        int percentOfStoreCountForLastYear = (int) (storeCountForLastYear * 1.1);

        double percentageOfStoreCount = percentOfStoreCountForLastYear != 0
                ? storeCount / (double) percentOfStoreCountForLastYear * 100
                : 0;
        model.addAttribute("storeCount", storeCount);
        model.addAttribute("percentOfStoreCountForLastYear", percentOfStoreCountForLastYear);
        model.addAttribute("percentageOfStoreCount", (int) Math.round(percentageOfStoreCount));

        // 가게별 매출
        //연도별
        List<SalesDataDTO> salesYearData = salesService.getTotalPriceByYear();
        List<Integer> salesYear = new ArrayList<>();
        List<Long> salesYearTotalPrice = new ArrayList<>();

        for (SalesDataDTO data : salesYearData) {
            salesYear.add(data.getDate());
            salesYearTotalPrice.add(data.getTotalPrice());
        }

        model.addAttribute("salesYear", salesYear);
        model.addAttribute("salesYearTotalPrice", salesYearTotalPrice);

        // 달별
        List<SalesDataDTO> salesMonthData = salesService.getTotalSalesForCurrentYearByMonth();
        List<Integer> salesMonth = new ArrayList<>();
        List<Long> salesMonthTotalPrice = new ArrayList<>();
        for (SalesDataDTO data : salesMonthData) {
            salesMonth.add(data.getDate());
            salesMonthTotalPrice.add(data.getTotalPrice());
        }
        model.addAttribute("salesMonth", salesMonth);
        model.addAttribute("salesMonthTotalPrice", salesMonthTotalPrice);

        return "admin/dashboard";
    }


    @GetMapping("/userManagement")
    public String userManagementPage(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        return "/admin/userManagement";
    }

    @GetMapping("/storeManagement")
    public String storeManagementPage(Model model) {
        List<StoreListDTO> storeList = storeService.getAllStoresWithUserNames();
        model.addAttribute("storeList", storeList);

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

    @GetMapping("/noticeList")
    public String noticePage(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<NoticeDTO> notices = noticeService.getNotices(page, size);

        model.addAttribute("notices", notices);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", notices.getTotalPages());
        model.addAttribute("pageSize", size);

        System.err.println(notices);
        return "/admin/noticeList";
    }

    @GetMapping("/notice")
    public String noticePage() {

        return "/admin/notice";
    }

    @ResponseBody
    @PostMapping("/notice")
    public ResponseEntity<?> noticePage(@ModelAttribute NoticeDTO noticeDTO) {
        try {
            noticeService.save(noticeDTO);
            return ResponseEntity.ok("작성을 완료했습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");
        }
    }

    @DeleteMapping("/notice/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Integer noticeId) {
        try {
            noticeService.delete(noticeId);
            return ResponseEntity.ok("완료을 완료했습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");
        }
    }

    @GetMapping("/contactList")
    public String contactPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<ContactDTO> contacts = contactService.getContacts(page, size);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", contacts.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/admin/contactList";
    }

    @GetMapping("/contact/{id}")
    public String contactPage(@PathVariable("id") Integer contactId, Model model) {
        model.addAttribute("contact", contactService.findById(contactId));
        return "/admin/response";
    }

    @ResponseBody
    @PostMapping("/response")
    public String contactPage(@RequestParam("contactId") int contactId, @RequestParam("answer") String answer, HttpSession session) {
        try {
            AdminDTO adminSession = (AdminDTO) session.getAttribute("adminSession");
            responseService.save(ResponseDTO.builder()
                    .contactId(contactId)
                    .author(adminSession.getName())
                    .content(answer).build());
            contactService.updateContactStatusById(contactId);
            return "완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "오류";
        }
    }


}
