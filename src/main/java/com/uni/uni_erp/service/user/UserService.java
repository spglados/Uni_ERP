package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.user.User;
import com.uni.uni_erp.dto.user.UserDTO;
import com.uni.uni_erp.dto.user.UserUpdateDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import com.uni.uni_erp.util.str.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        try {
            user.setPassword(PasswordUtil.hashGenerator(user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userRepository.save(user);
    }

    public User login(UserDTO.loginDTO dto) {
        try {
            String storedSaltedHash = userRepository.findPasswordByEmail(dto.getEmail());
            if (PasswordUtil.verify(dto.getPassword(), storedSaltedHash)) {
                return userRepository.findByEmail(dto.getEmail());
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String findByEmail(String email) {
        return userRepository.findPasswordByEmail(email);
    }

    public User findById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));
        return user;
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
        return userRepository.countByMembership(User.Membership.PREMIUM);
    }

    // 작년 구독자수
    public int getPremiumUserCountForLastYear() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusYears(1).withMonth(1).withDayOfMonth(1); // 작년 1월 1일
        LocalDate endDate = now.minusYears(1).withMonth(12).withDayOfMonth(31); // 작년 12월 31일

        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay().minusNanos(1)); // 12월 31일의 마지막 순간

        Integer subscriberCount = userRepository.countByMembershipAndCreatedAtBetween(User.Membership.PREMIUM, startTimestamp, endTimestamp);

        return subscriberCount;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUserEmailByUserId(String email, int userId) {
        userRepository.updateEmailByUserId(email, userId);
    }

    @Transactional
    public void updateUserPhoneByUserId(String phone, int userId) {
        userRepository.updatePhoneByUserId(phone, userId);
    }

    @Transactional
    public void updateUserAddressByUserId(String address, int userId) {
        userRepository.updateAddressByUserId(address, userId);
    }

    @Transactional
    public void updateUserPaymentDateByUserId(String paymentDateStr, int userId) {
        // paymentDate를 Integer로 변환
        int paymentDate = Integer.parseInt(paymentDateStr);

        // 임시값으로 설정할 날짜
        LocalDate now = LocalDate.now();
        LocalDate updatedPaymentDate;

        // 주어진 day가 현재 날짜의 day보다 이전인지 확인
        if (paymentDate < now.getDayOfMonth()) {
            // 이전일 경우 다음 달로 설정
            updatedPaymentDate = now.plusMonths(1);
        } else {
            // 이후일 경우 이번 달로 설정
            updatedPaymentDate = now;
        }

        // paymentDate를 설정하되, 유효하지 않다면 마지막 날로 설정
        if (paymentDate > updatedPaymentDate.lengthOfMonth()) {
            updatedPaymentDate = updatedPaymentDate.withDayOfMonth(updatedPaymentDate.lengthOfMonth());
        } else {
            updatedPaymentDate = updatedPaymentDate.withDayOfMonth(paymentDate);
        }

        // 날짜를 yyyy-MM-dd 형식으로 변환
        String formattedPaymentDate = updatedPaymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        userRepository.updatePaymentDateByUserId(formattedPaymentDate, userId);
    }

    public Long countUsers() {
        return userRepository.count();
    }

    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }

    public void updateUser(Integer userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. userId: " + userId));

        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setPhone(userUpdateDTO.getPhone());
        user.setAddress(userUpdateDTO.getAddress());
        if (userUpdateDTO.getMembership().equals("COMMON") || userUpdateDTO.getMembership().equals("PREMIUM")) {
            user.setMembership(userUpdateDTO.getMembership().equals("COMMON") ? User.Membership.COMMON : User.Membership.PREMIUM);
        }
        userRepository.save(user);

    }

    public String getUserMembership(Integer userId) {
        return userRepository.findMembershipByUserId(userId);
    }
}
