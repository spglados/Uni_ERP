package com.uni.uni_erp.schedule;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.repository.user.StoreRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final StoreRepository storeRepository;
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "*/10 * * * * ?")
    public void cleanUpUsers() {
        List<User> users = userRepository.findAll(); // 모든 사용자 조회
        for (User user : users) {
            if (
                    user.getPremiumToCommonDate() != null &&
                    //user.getPremiumToCommonDate().isBefore(LocalDateTime.now().minusSeconds(30))) {
                    user.getPremiumToCommonDate().isBefore(LocalDateTime.now().minusMonths(1))) {

                int paymentCount = paymentRepository.countPaymentsByUserIdAndStatus(user.getId());
                if (paymentCount == 0) {
                    // 결제 내역이 없으면 스토어와 임플로이를 삭제
                    //storeRepository.deleteByUserId(user.getId());
                    System.out.println("스토어가 삭제되었습니다: " + user.getId());
                } else {
                    System.out.println("결제 내역이 있어 삭제되지 않았습니다: " + user.getId());
                }
            }
        }
    }
}
