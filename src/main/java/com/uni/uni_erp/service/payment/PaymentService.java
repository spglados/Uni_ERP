package com.uni.uni_erp.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.dto.PaymentDTO;
import com.uni.uni_erp.repository.user.PaymentRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${payment.secretKey}")
    private String secretKey;

    @Transactional
    public String authorizeBillingAndAutoPayment(String authKey, String customerKey, String orderId, String orderName,
                                                 Integer amount, Integer userPk) throws Exception {
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
                String nextDate = simpleDateFormat.format(cal.getTime());


                User user = userRepository.findById(userPk)
                        .orElseThrow(() -> new RuntimeException("User not found"));




                // 기존 총 결제 금액 가져오기
                Integer existingTotalAmount = paymentRepository.sumAmountByUserId(userPk); // 해당 사용자 총 결제 금액

                // 새로운 결제 금액을 기존 총 금액에 더하기
                Integer newTotalAmount = (existingTotalAmount != null ? existingTotalAmount : 0) + amount;

                //TODO -  추가한 코드
                /*Integer adjustedAmount;

                Integer previousAmount = paymentRepository.findLatestPaymentAmountByUserId(userPk); // 이전 결제 금액

                // 첫 번째 정기 결제일 경우
                if (previousAmount == null) {
                    // 남은 일수 계산
                    Calendar today = Calendar.getInstance();
                    int remainingDays = today.getActualMaximum(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH);
                    adjustedAmount = (int) (newTotalAmount * (remainingDays / (double) today.getActualMaximum(Calendar.DAY_OF_MONTH)));
                } else {
                    // 두 번째 정기 결제는 이전 결제 금액 그대로
                    adjustedAmount = previousAmount;
                }*/

                // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //


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
                        .totalAmount(newTotalAmount.toString())
                        .requestedAt(paymentJson.get("requestedAt").asText())
                        .approvedAt(paymentJson.get("approvedAt").asText())
                        .cancel("N")
                        .nextPay(nextDate)
                        //.nextPayAmount(adjustedAmount)

                        .build();

                user.setMembership(User.Membership.PREMIUM);
                paymentRepository.save(paymentDTO.toPayment(user));
                //paymentRepository.insertSubscribing(paymentDTO.toSubscribing());

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
