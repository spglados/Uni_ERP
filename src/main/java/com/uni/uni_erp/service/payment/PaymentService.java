package com.uni.uni_erp.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import com.uni.uni_erp.dto.PaymentDTO;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.repository.payment.PaymentHistoryRepository;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Value("${payment.secretKey}")
    private String secretKey;

    @Transactional
    public String authorizeBillingAndAutoPayment(String authKey, String customerKey, String orderId, String orderName,
                                                 Integer amount, Integer userPk,String desiredPayDate) throws Exception {
        String encodedAuthHeader = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());


        // 빌링키 발급과 동시에 자동 결제 수행
        HttpRequest billingRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
                .header("Authorization", "Basic " + encodedAuthHeader).header("Content-Type", "application/json")
                .method("POST",
                        HttpRequest.BodyPublishers
                                .ofString("{\"authKey\":\"" + authKey + "\",\"customerKey\":\"" + customerKey + "\"}"))
                .build();

        HttpResponse<String> billingResponse = HttpClient.newHttpClient().send(billingRequest,
                HttpResponse.BodyHandlers.ofString());

        if (billingResponse.statusCode() == 200) {
            JsonNode billingJson = objectMapper.readTree(billingResponse.body());
            String billingKey = billingJson.get("billingKey").asText();

            // 자동 결제 요청
            HttpRequest paymentRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/" + billingKey))
                    .header("Authorization", "Basic " + encodedAuthHeader).header("Content-Type", "application/json")
                    .method("POST",
                            HttpRequest.BodyPublishers.ofString(
                                    "{\"customerKey\":\"" + customerKey + "\"," + "\"orderId\":\"" + orderId + "\","
                                            + "\"orderName\":\"" + orderName + "\"," + "\"amount\":" + amount + "}"))
                    .build();

            HttpResponse<String> paymentResponse = HttpClient.newHttpClient().send(paymentRequest,
                    HttpResponse.BodyHandlers.ofString());

            if (paymentResponse.statusCode() == 200) {
                JsonNode paymentJson = objectMapper.readTree(paymentResponse.body());




                // 다음 결제일 계산
                String dateFormatType = "yyyy-MM-dd";
                Date toDay = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType);
                Calendar cal = Calendar.getInstance();
                cal.setTime(toDay);
                cal.add(Calendar.MONTH, +1);
                // 결제일을 원하는 날짜로 설정
                String nextDate = desiredPayDate != null ? desiredPayDate : simpleDateFormat.format(cal.getTime());
                // String nextDate = simpleDateFormat.format(cal.getTime());


                User user = userRepository.findById(userPk)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 지금바로 결제금액
                int initialAmount = 0;
                User.Membership membership = user.getMembership();
                if(membership == User.Membership.COMMON){
                    initialAmount = 50000;
                } else {
                    initialAmount = 30000;
                }

                // 현재 날짜 가져오기
                Calendar today = Calendar.getInstance();
                // 해당 월의 최대 일수
                int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                // 현재 날짜
                int currentDay = today.get(Calendar.DAY_OF_MONTH);
                // 남은 일수 계산
                int remainingDays = maxDaysInMonth - currentDay;
                // 남은 일수에 따라 조정된 금액 계산
                double remainingAmount = initialAmount * (remainingDays / (double) maxDaysInMonth);
                // 조정된 금액계산 포매팅
                int formatNextPayAmount = (int) Math.floor(remainingAmount);
                int nowPay = initialAmount + formatNextPayAmount;


                int nextPay = 0;
                if (paymentRepository.sumAmountByUserId(userPk) == null) {
                    nextPay = initialAmount;
                } else {
                    nextPay = paymentRepository.sumAmountByUserId(userPk) + initialAmount;
                }



                // ==


                // DTO 변환
                PaymentDTO.RegularPaymentDTO paymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                        .userId(userPk)
                        .lastTransactionKey(paymentJson.get("lastTransactionKey").asText())
                        .paymentKey(paymentJson.get("paymentKey").asText())
                        .orderId(paymentJson.get("orderId").asText())
                        .orderName2(paymentJson.get("orderName").asText())
                        .billingKey(billingKey)
                        .customerKey(customerKey)
                        .amount(amount)
                        .totalAmount("")
                        .requestedAt(paymentJson.get("requestedAt").asText())
                        .approvedAt(paymentJson.get("approvedAt").asText())
                        .cancel("N")
                        .nextPay(nextDate)
                        .nowPayAmount(nowPay) // 지금 당장 결제
                        .nextPayAmount(nextPay) // 다음달 결제
                        .build();

                user.setMembership(User.Membership.PREMIUM);
                paymentRepository.save(paymentDTO.toPayment(user));
                //paymentRepository.insertSubscribing(paymentDTO.toSubscribing());

                 //TODO -코드추가
                PaymentHistory paymentHistory = PaymentHistory.builder()
                        .user(user) // 현재 사용자
                        .payment(paymentRepository.findById(userPk).orElse(null)) // 해당 결제 정보
                        .amount(amount)
                        .status("SUCCESS") // 결제 성공
                        .transactionId("") // 거래 ID
                        .paymentMethod("카드") // 결제 방법 (상황에 맞게 조정)
                        .historyStatus(1)
                        .build();

                // PaymentHistory 저장
                //paymentHistoryRepository.save(paymentHistory);


                return paymentJson.toPrettyString();
            } else {

                // DTO 변환
                PaymentDTO.RegularPaymentDTO errorPaymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                        .userId(userPk).customerKey(customerKey)
                        .billingKey(billingKey)
                        .amount(amount)
                        .orderId(orderId)
                        .orderName(orderName)
                        .billingErrorCode(billingResponse.statusCode())
                        .payErrorCode(paymentResponse.statusCode())
                        .build();
                //paymentRepository.insertOrder(errorPaymentDTO.toOrder());

                throw new RuntimeException(paymentResponse.body());
            }
        } else {
            throw new RuntimeException(billingResponse.body());
        }
    }

}
