<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/payment/payment.css">
<script src="https://js.tosspayments.com/v1"></script>
<main class="main-container">

  <section class="premium-section">
    <h2>프리미엄 정액제 구독</h2>
    <p>한달에 단돈 29,900원으로 프리미엄 서비스를 마음껏 이용하세요. 고급스럽고 편리한 기능을 통해 더 나은 경험을 제공합니다.</p>
  </section>

  <section class="payment-details">
    <h3>결제 정보 입력</h3>

    <button id="card_button" onclick="Pay()">정기결제</button>
    <div class="form-group">
      <label for="desiredDay">다음 결제일:</label>
      <input type="number" id="desiredDay" name="desiredDay" min="1" max="31" placeholder="1-31" required>
    </div>
  </section>

</main>

<script>
    // 정기결제
    function Pay() {
        const clientKey = "test_ck_E92LAa5PVbPWkE0RkGbW87YmpXyJ"; // 서버에서 전달받은 클라이언트 키
        const tossPayments = TossPayments(clientKey);
        const customerKey = Math.random().toString(36).substring(2, 12); // 고객 고유키를 서버로부터 받아옵니다.

        const desiredDay = document.getElementById("desiredDay").value; // 사용자가 입력한 원하는 날짜
        console.log("desiredDay", desiredDay);

        tossPayments.requestBillingAuth("카드", {
            customerKey: customerKey, // 서버에서 전달받은 고객 키
            successUrl: "http://localhost:8080/payment/success?desiredPayDate=" + desiredDay, // 성공 시 리디렉션 URL
            failUrl: "http://localhost:8080/payment/fail" // 실패 시 리디렉션 URL
        })
        .catch(function (error) {
            if (error.code === "USER_CANCEL") {
                alert("결제를 취소했습니다.");
            } else if (error.code === "INVALID_CARD_COMPANY") {
                alert(error.message);
            }
        });
    }
</script>

<%@include file="/WEB-INF/view/layout/footer.jsp"%>
