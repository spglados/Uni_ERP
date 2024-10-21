package com.uni.uni_erp.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import com.uni.uni_erp.domain.entity.payment.RefundRepository;
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
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final RefundRepository refundRepository;

    @Value("${payment.secretKey}")
    private String secretKey;

    @Transactional
    public String authorizeBillingAndAutoPayment(String authKey, String customerKey, String orderId, String orderName,
                                                 Integer amount, Integer userPk, String desiredPayDate) throws Exception {
        String encodedAuthHeader = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        // 빌링키 발급과 동시에 자동 결제 수행
        HttpRequest billingRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
                .header("Authorization", "Basic " + encodedAuthHeader)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"authKey\":\"" + authKey + "\",\"customerKey\":\"" + customerKey + "\"}"))
                .build();

        HttpResponse<String> billingResponse = HttpClient.newHttpClient().send(billingRequest, HttpResponse.BodyHandlers.ofString());

        if (billingResponse.statusCode() == 200) {
            JsonNode billingJson = objectMapper.readTree(billingResponse.body());
            String billingKey = billingJson.get("billingKey").asText();

            // 자동 결제 요청
            HttpRequest paymentRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/" + billingKey))
                    .header("Authorization", "Basic " + encodedAuthHeader)
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(
                            "{\"customerKey\":\"" + customerKey + "\"," + "\"orderId\":\"" + orderId + "\"," +
                                    "\"orderName\":\"" + orderName + "\"," + "\"amount\":" + amount + "}"))
                    .build();

            HttpResponse<String> paymentResponse = HttpClient.newHttpClient().send(paymentRequest, HttpResponse.BodyHandlers.ofString());

            if (paymentResponse.statusCode() == 200) {
                JsonNode paymentJson = objectMapper.readTree(paymentResponse.body());

                // 현재 날짜 가져오기
                Calendar today = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = sdf.format(today.getTime());
                User user = userRepository.findById(userPk)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 지금 당장 결제할 금액 계산
                int initialAmount = (user.getMembership() == User.Membership.COMMON) ? 50000 : 30000;
                int currentDay = today.get(Calendar.DAY_OF_MONTH);
                int day = (desiredPayDate != null && !desiredPayDate.isEmpty()) ? Integer.parseInt(desiredPayDate) : currentDay; //결제일

                double nowPayAmount = 0;

                // 다음 결제일 계산
                Calendar nextPayDate = Calendar.getInstance();
                nextPayDate.set(Calendar.MONTH, today.get(Calendar.MONTH) + 1);
                nextPayDate.set(Calendar.DAY_OF_MONTH, day);
                String nextDate = String.format("%d-%02d-%02d", nextPayDate.get(Calendar.YEAR), nextPayDate.get(Calendar.MONTH) + 1, day);
                System.out.println(nextDate+"tjclcl");
                // 다음 달 결제 금액 계산
                int nextPay = (paymentRepository.sumAmountByUserId(userPk) == null) ? initialAmount : paymentRepository.sumAmountByUserId(userPk) + initialAmount;

                int latestAmount = paymentHistoryRepository.findLatestAmountByUserId(userPk)
                        .orElse(0); // null일 경우 기본값 0 사용
                System.out.println(latestAmount+"tjcldnjs");






                     // 원하는 결제일
                int desiredPaymentDay = Integer.parseInt(desiredPayDate);

                if (user.getMembership() == User.Membership.COMMON) {
                    initialAmount = 50000;
                    if (day < currentDay) {
                        // 남은 일수에 비례한 금액 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        double remainingAmount = initialAmount * (remainingDays / (double) maxDaysInMonth);

                        String paymentDate = user.getPaymentDate();
                        if (paymentDate != null) {
                            int userPaymentDay = Integer.parseInt(paymentDate.split("-")[2]); // "2024-11-13" -> 13
                            if (userPaymentDay > desiredPaymentDay) {
                                nowPayAmount = Math.floor(remainingAmount) + initialAmount + latestAmount; // 추가 결제 금액
                            } else {
                                nowPayAmount = Math.floor(remainingAmount) + initialAmount; // 이번 달 결제 + 다음 달 결제 금액
                            }
                        }
                        user.setPaymentDate(nextDate);
                    } else {
                        // 이번 달의 비례 금액만 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        nowPayAmount = Math.floor(initialAmount * (remainingDays / (double) maxDaysInMonth)); // 비례 금액만
                        user.setPaymentDate(nextDate);
                    }
                } else if (user.getMembership() == User.Membership.PREMIUM) {
                    initialAmount = 30000;
                    if (day < currentDay) {
                        // 남은 일수에 비례한 금액 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        double remainingAmount = initialAmount * (remainingDays / (double) maxDaysInMonth);

                        String paymentDate = user.getPaymentDate();
                        if (paymentDate != null) {
                            int userPaymentDay = Integer.parseInt(paymentDate.split("-")[2]); // "2024-11-13" -> 13
                            if (userPaymentDay > desiredPaymentDay) {
                                nowPayAmount = Math.floor(remainingAmount) + initialAmount + latestAmount; // 추가 결제 금액
                            } else {
                                nowPayAmount = Math.floor(remainingAmount) + initialAmount; // 이번 달 결제 + 다음 달 결제 금액
                            }
                        }
                        user.setPaymentDate(nextDate);
                    } else {
                        // 이번 달의 비례 금액만 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        nowPayAmount = Math.floor(initialAmount * (remainingDays / (double) maxDaysInMonth)); // 비례 금액만
                        user.setPaymentDate(nextDate);
                    }
                }


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
                        .nextPay(nextDate) // 다음 결제일
                        .nowPayAmount((int) nowPayAmount) // 지금 당장 결제
                        .nextPayAmount(nextPay) // 다음 달 결제
                        .date(dateString)
                        .build();

                user.setMembership(User.Membership.PREMIUM);
                // 결제 정보 저장
                paymentRepository.save(paymentDTO.toPayment(user));

                // 결제 히스토리 저장
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
                paymentHistoryRepository.save(paymentHistory);

                return paymentJson.toPrettyString();
            } else {
                throw new RuntimeException(paymentResponse.body());
            }
        } else {
            throw new RuntimeException(billingResponse.body());
        }
    }


    @Transactional
    public String cancelPayment(String paymentKey, String cancelReason, int adminId, int payPk) throws Exception {

        String encodedAuthHeader = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        // 결제 취소 요청
        HttpRequest cancelRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", "Basic " + encodedAuthHeader)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객 변심\"}"))
                .build();

        HttpResponse<String> cancelResponse = HttpClient.newHttpClient().send(cancelRequest,
                HttpResponse.BodyHandlers.ofString());

        if ("200".equalsIgnoreCase(String.valueOf(cancelResponse.statusCode()))) {
            JsonNode cancelJson = objectMapper.readTree(cancelResponse.body());

            // DTO 변환
            PaymentDTO.RegularPaymentDTO paymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                    // TODO - 관리자 pk 입력 예정
                    .lastTransactionKey(cancelJson.get("lastTransactionKey").asText())
                    .paymentKey(paymentKey)
                    .cancelReason(cancelReason)
                    .requestedAt(cancelJson.get("requestedAt").asText())
                    .approvedAt(cancelJson.get("approvedAt").asText())
                    .cancelAmount("10000")
                    .adminId(adminId)
                    .build();

            refundRepository.save(paymentDTO.toRefund());
            //paymentRepository.update(payPk); // payment_tb에 cancel 유무 업데이트

            return cancelJson.toPrettyString();
        } else {
            throw new RuntimeException(cancelResponse.body());
        }
    }

}
