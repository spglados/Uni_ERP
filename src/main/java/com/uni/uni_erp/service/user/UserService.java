package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.PrincipalDTO;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    public void save(User user) {

        userRepository.save(user);
    }

    public User login(UserDTO.JoinDTO dto) {
        return userRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
    }

    public User findById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));
        return user;
    }

    public PrincipalDTO searchUserId(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다."));
        PrincipalDTO principalDTO = PrincipalDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .membership(user.getMembership())
                .createdAt(user.getCreatedAt())
                .build();

        return principalDTO;
    }


    public boolean checkDuplicateEmail(String email) {
        boolean isUse = userRepository.existsByEmail(email);
        return isUse;
    }

    public boolean checkDuplicatePhone(String phone) {
        boolean isUse = userRepository.existsByPhone(phone);
        return isUse;
    }

    public void certifiedPhoneNumber(String userPhoneNumber, int randomNumber) {
        Message coolsms = new Message(apiKey, apiSecret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", userPhoneNumber);    // 수신전화번호
        params.put("from", fromPhoneNumber);    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[TEST] 인증번호는" + "[" + randomNumber + "]" + "입니다."); // 문자 내용 입력
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }

    // 구독자 수
    public int getPremiumUserCount() {
        Integer subscribeUserCount = userRepository.countByMembership(User.Membership.PREMIUM);
        return subscribeUserCount;
    }

    // 작년 구독자수
    public int getPremiumUserCountForLastYear() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusYears(1).withDayOfMonth(1).withMonth(1); // 작년 1월 1일
        LocalDate endDate = now.minusYears(1).withDayOfMonth(31).withMonth(12); // 작년 12월 31일

        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay().minusNanos(1)); // 12월 31일의 마지막 순간

        Integer subscriberCount = userRepository.countByMembershipAndCreatedAtBetween(User.Membership.PREMIUM, startTimestamp, endTimestamp);

        return subscriberCount;
    }



    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void cleanUpUsers() {
        List<User> users = userRepository.findAll(); // 모든 사용자 조회
        for (User user : users) {
            if (user.getPreviousMembership() != null &&
                    user.getPreviousMembership().equals("COMMON") &&
                    user.getPremiumToCommonDate() != null &&
                    user.getPremiumToCommonDate().isBefore(LocalDateTime.now().minusMonths(1))) {

                int paymentCount = paymentRepository.countPaymentsByUserIdAndStatus(user.getId());

                if (paymentCount == 0) {
                    // 결제 내역이 없으면 스토어와 임플로이를 삭제
                    // storeService.deleteByUserId(user.getId());
                    // employeeService.deleteByUserId(user.getId());
                    System.out.println("스토어와 임플로이가 삭제되었습니다: " + user.getId());
                } else {
                    // 결제 내역이 하나라도 있으면 삭제하지 않음
                    System.out.println("결제 내역이 있어 삭제되지 않았습니다: " + user.getId());
                }
            }
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
