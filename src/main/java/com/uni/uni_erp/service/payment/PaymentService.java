package com.uni.uni_erp.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.payment.Payment;
import com.uni.uni_erp.domain.entity.payment.PaymentHistory;
import com.uni.uni_erp.dto.PaymentDTO;
import com.uni.uni_erp.repository.payment.PaymentHistoryRepository;
import com.uni.uni_erp.repository.payment.PaymentRepository;
import com.uni.uni_erp.repository.refund.RefundRepository;
import com.uni.uni_erp.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.List;
import java.util.Map;

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

                // 다음 달 결제 금액 계산
                int nextPay = (paymentRepository.sumAmountByUserId(userPk) == null) ? initialAmount : paymentRepository.sumAmountByUserId(userPk) + initialAmount;

                int latestAmount = paymentHistoryRepository.findLatestAmountByUserId(userPk)
                        .orElse(0); // null일 경우 기본값 0 사용


                // 원하는 결제일
                int desiredPaymentDay = Integer.parseInt(desiredPayDate);

                int status = 1;
                String odername = null;

                if (user.getMembership() == User.Membership.COMMON) {
                    initialAmount = 50000;
                    orderName = "첫번째 결제";
                    if (day < currentDay) {
                        // 남은 일수에 비례한 금액 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        double remainingAmount = initialAmount * (remainingDays / (double) maxDaysInMonth);
                        nowPayAmount = Math.floor(remainingAmount) + initialAmount;

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
                        status = -1;
                        user.setPaymentDate(nextDate);
                    }
                } else if (user.getMembership() == User.Membership.PREMIUM) {
                    initialAmount = 30000;
                    orderName = "두번째 결제";
                    if (day < currentDay) {
                        // 남은 일수에 비례한 금액 계산
                        int maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int remainingDays = maxDaysInMonth - currentDay;
                        double remainingAmount = initialAmount * (remainingDays / (double) maxDaysInMonth);
                        nowPayAmount = Math.floor(remainingAmount) + initialAmount;

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
                        status = -1;
                    }
                }

                // DTO 변환
                PaymentDTO.RegularPaymentDTO paymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                        .userId(userPk)
                        .lastTransactionKey(paymentJson.get("lastTransactionKey").asText())
                        .paymentKey(paymentJson.get("paymentKey").asText())
                        .orderId(paymentJson.get("orderId").asText())
                        .orderName(orderName)
                        .billingKey(billingKey)
                        .customerKey(customerKey)
                        .amount(amount)
                        .totalAmount("")
                        .method("카드")
                        .requestedAt(paymentJson.get("requestedAt").asText())
                        .approvedAt(paymentJson.get("approvedAt").asText())
                        .cancel("N")
                        .nextPay(nextDate) // 다음 결제일
                        .nowPayAmount((int) nowPayAmount) // 지금 당장 결제
                        .nextPayAmount(nextPay) // 다음 달 결제
                        .date(dateString)
                        .status(status)
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
    public int cancelAndCalculateAmount(List<Map<String, String>> paymentRequests) throws Exception {
        int totalCancelAmount = 0;

        for (Map<String, String> paymentRequest : paymentRequests) {
            String paymentKey = paymentRequest.get("paymentKey");
            String cancelReason = paymentRequest.get("cancelReason");
            String payPk = paymentRequest.get("payPk");

            int payPkint = Integer.parseInt(payPk);
            String encodedAuthHeader = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

            // 결제 취소 요청
            HttpRequest cancelRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .header("Authorization", "Basic " + encodedAuthHeader)
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
                    .build();

            HttpResponse<String> cancelResponse = HttpClient.newHttpClient().send(cancelRequest, HttpResponse.BodyHandlers.ofString());

            if ("200".equals(String.valueOf(cancelResponse.statusCode()))) {
                JsonNode cancelJson = objectMapper.readTree(cancelResponse.body());

                // 결제 정보 조회
                Payment payment = paymentRepository.findById(payPkint)
                        .orElseThrow(() -> new RuntimeException("Payment not found with id: " + payPkint));
                int paymentStatusCount = paymentRepository.countPaymentsByUserIdAndStatus(1); // 예시로 userId 1로 설정
                int cancelAmount = 0;

                User user = userRepository.findById(1).orElseThrow(() -> new RuntimeException(""));

                List<Payment> 결제내역들 = findByUserId(1);

                // 상태 및 조건에 따른 환불 금액 계산
                boolean hasNegativeOne = 결제내역들.stream().anyMatch(payments -> payments.getStatus() == -1); // -1 상태 확인
                boolean 일이존재할때 = 결제내역들.stream().anyMatch(payments -> payments.getStatus() == 1); // 1 상태 확인
                long 마이너스일갯수 = 결제내역들.stream().filter(payments -> payments.getStatus() == -1).count(); // -1 상태 개수 확인

                if (paymentStatusCount > 1) {
                    if (hasNegativeOne) {
                        if (일이존재할때) {
                            if (마이너스일갯수 >= 2) {
                                cancelAmount = 0;
                                paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                                payment.setStatus(0);
                            } else {
                                cancelAmount = 30000;
                                paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                                payment.setStatus(0);
                            }
                        } else {
                            cancelAmount = 0;
                            paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                            payment.setStatus(0);
                        }
                    } else {
                        cancelAmount = 30000;
                        payment.setStatus(0);
                    }
                } else if (paymentStatusCount == 1) {
                    if (hasNegativeOne) {
                        cancelAmount = 0;
                        paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                        payment.setStatus(0);
                        user.setMembership(User.Membership.COMMON);

                    } else {
                        cancelAmount = 50000;
                        payment.setStatus(0);
                        user.setMembership(User.Membership.COMMON);

                    }
                }

                // DTO 변환 및 저장
                PaymentDTO.RegularPaymentDTO paymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                        .lastTransactionKey(cancelJson.get("lastTransactionKey").asText())
                        .paymentKey(paymentKey)
                        .cancelReason(cancelReason)
                        .requestedAt(cancelJson.get("requestedAt").asText())
                        .approvedAt(cancelJson.get("approvedAt").asText())
                        .cancelAmount(String.valueOf(cancelAmount))
                        .build();

                refundRepository.save(paymentDTO.toRefund());
                paymentRepository.updateCancel(payPkint); // payment_tb에 cancel 유무 업데이트

                totalCancelAmount += cancelAmount; // 총 환불 금액에 추가
            } else {
                throw new RuntimeException(cancelResponse.body());
            }
        }

        return totalCancelAmount; // 총 환불 금액 반환
    }





    // 환불
    //TODO 되살려주기
    /*@Transactional
    public String cancelPayment(String paymentKey, String cancelReason, String payPk) throws Exception {
        int payPkint = Integer.parseInt(payPk);

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

        JsonNode cancelJson = null;
        if ("200".equalsIgnoreCase(String.valueOf(cancelResponse.statusCode()))) {
            cancelJson = objectMapper.readTree(cancelResponse.body());

            //TODO - 환불 금액 로직 짜야함.
            // 1. 결제한 날짜가 정기결제일 보다 이전일때만 환불금액이있음. 아니면 없음
            // 2. (1번조건을 맞춘 상태)  오늘날짜의 달이 결제한 날짜의 달과 같다면 환불금액 있음

            // 유저의 정기결제일 뽑기
            User user = userRepository.findById(1).orElseThrow(() -> new RuntimeException(""));

            // 체크된 결제내역 뽑기
            Payment payment = paymentRepository.findById(payPkint)
                    .orElseThrow(() -> new RuntimeException("Payment not found with id: " + payPkint));

            int paymentStatusCount = paymentRepository.countPaymentsByUserIdAndStatus(1);

            Integer cancelAmount = 0;

            List<Payment> 결제내역들 = findByUserId(1);

            boolean hasNegativeOne = 결제내역들.stream().anyMatch(payments -> payments.getStatus() == -1);

            boolean 일이존재할때 = 결제내역들.stream().anyMatch(payments -> payments.getStatus() == 1);

            long 마이너스일갯수 = 결제내역들.stream().filter(payments -> payments.getStatus() == -1).count();

            //TODO 1. 결제를 한번만 했을때 이번달에 비례한 금액만 있을때 cancelAmount = 0;


            // 1보다 크면 3만원 결제
            if (paymentStatusCount > 1) {
                if(hasNegativeOne) {
                    if(일이존재할때){
                        if(마이너스일갯수 >=2){
                            cancelAmount = 0;
                            paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                            payment.setStatus(0);
                        } else {
                            cancelAmount = 30000;
                            paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                            payment.setStatus(0);
                        }
                    } else {
                        cancelAmount = 0;
                        paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                        payment.setStatus(0);
                    }
                } else {
                    cancelAmount = 30000;
                    payment.setStatus(0);
                }
                // 1이면 5만원 결제
            } else if (paymentStatusCount == 1) {
                if(hasNegativeOne) {
                    cancelAmount = 0;
                    paymentRepository.updateLatestPaymentStatusToOne(); // 가장 최신 -1인걸 1로 바꾸기
                    payment.setStatus(0);
                    user.setMembership(User.Membership.COMMON);
                } else {
                    cancelAmount = 50000;
                    payment.setStatus(0);
                    user.setMembership(User.Membership.COMMON);
                }
            }

            String cancelAmountStr = String.valueOf(cancelAmount);
            //TODO - 여기까지


            // DTO 변환
            PaymentDTO.RegularPaymentDTO paymentDTO = PaymentDTO.RegularPaymentDTO.builder()
                    .lastTransactionKey(cancelJson.get("lastTransactionKey").asText())
                    .paymentKey(paymentKey)
                    .cancelReason(cancelReason)
                    .requestedAt(cancelJson.get("requestedAt").asText())
                    .approvedAt(cancelJson.get("approvedAt").asText())
                    .cancelAmount(cancelAmountStr)
                    .build();

            refundRepository.save(paymentDTO.toRefund());
            paymentRepository.updateCancel(payPkint); // payment_tb에 cancel 유무 업데이트

            return cancelJson.toPrettyString();
        } else {
            throw new RuntimeException(cancelResponse.body());
        }
    }*/

    public Payment findById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    public Integer getCountOfNotCanceledPayments(Integer userId) {
        return paymentRepository.countByCancelN(userId);
    }

    public List<Payment> findByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Integer getCountOfPaymentsWithStatusNotZero(Integer userPk) {
        return paymentRepository.countByStatusNotZero(userPk);
    }
}
