<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/payment/payment.css">
<script src="https://js.tosspayments.com/v1"></script>
<main class="main-container">

  <section class="premium-section">
    <h2>프리미엄 정액제 구독</h2>
    <p>첫 결제는 50,000원, 이후부터는 매달 30,000원으로 프리미엄 서비스를 마음껏 이용하세요. 고급스럽고 편리한 기능을 통해 더 나은 경험을 제공합니다.</p>
    <p>주의사항: 모든 결제를 취소하면 다음 결제 시 첫 결제는 50,000원으로 시작하며, 이후 매달 30,000원이 청구됩니다.</p>
  </section>

  <section class="payment-details">


    <div class="form-group">
      <label for="desiredDay">다음 결제일:</label>
          <select id="desiredDay" name="desiredDay" required>
            <!-- 1일부터 31일까지 옵션 생성 -->
            <% for (int day = 1; day <= 31; day++) { %>
              <option value="<%= day %>"><%= day %>일</option>
            <% } %>
          </select>
    </div>
    <button id="card_button" onclick="Pay()"style="display: block; margin-bottom: 10px;">결제하기</button>
  </section>

</main>

<script>
    window.onload = function() {
        const today = new Date();
        const todayDay = today.getDate(); // 오늘의 일수

        const daySelect = document.getElementById("desiredDay");
        daySelect.value = todayDay; // 오늘의 일수를 선택 상태로 설정
    };

    // 정기결제
    function Pay() {
        const clientKey = "test_ck_E92LAa5PVbPWkE0RkGbW87YmpXyJ"; // 서버에서 전달받은 클라이언트 키
        const tossPayments = TossPayments(clientKey);
        const customerKey = Math.random().toString(36).substring(2, 12); // 고객 고유키를 서버로부터 받아옵니다.

         const desiredDay = document.getElementById("desiredDay").value;

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
