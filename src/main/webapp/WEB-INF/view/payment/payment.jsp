<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 8:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/payment/payment.css">
<main class="main-container">

  <section class="premium-section">
    <h2>프리미엄 정액제 구독</h2>
    <p>한달에 단돈 29,900원으로 프리미엄 서비스를 마음껏 이용하세요. 고급스럽고 편리한 기능을 통해 더 나은 경험을 제공합니다.</p>
  </section>

  <section class="payment-details">
    <h3>결제 정보 입력</h3>
    <form action="/payment/subscribe" method="post">
      <div class="form-group">
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required>
      </div>
      <div class="form-group">
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-group">
        <label for="card-number">카드 번호:</label>
        <input type="text" id="card-number" name="cardNumber" required>
      </div>
      <div class="form-group">
        <label for="expiry-date">유효기간:</label>
        <input type="text" id="expiry-date" name="expiryDate" placeholder="MM/YY" required>
      </div>
      <div class="form-group">
        <label for="cvv">CVV:</label>
        <input type="text" id="cvv" name="cvv" required>
      </div>
      <button type="submit" class="subscribe-button">구독하기</button>
    </form>
  </section>

</main>

<%@include file="/WEB-INF/view/layout/footer.jsp"%>