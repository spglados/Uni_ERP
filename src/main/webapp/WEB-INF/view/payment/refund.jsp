<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<h1>환불 페이지</h1>
<table>
    <thead>
        <tr>
            <th>선택</th>
            <th>순서</th>
            <th>주문 이름</th>
            <th>결제 금액</th>
            <th>지금 결제 금액</th>
            <th>다음 결제 금액</th>
            <th>다음 결제일</th>
            <th>취소 여부</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="payment" items="${payments}">
            <tr>
                <td><input type="checkbox" class="payment-checkbox" value="${payment.id}" /></td>
                <td>${payment.id}</td>
                <td>${payment.orderName}</td>
                <td>${payment.amount}</td>
                <td>${payment.nowPayAmount}</td>
                <td>${payment.nextPayAmount}</td>
                <td>${payment.nextPay}</td>
                <td>${payment.cancel}</td>
                <td>
                    <input type="hidden" class="payment-key" value="${payment.paymentKey}" />
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<input type="text" id="cancelReason" placeholder="취소 사유" />
<button onclick="cancelPayments()">환불 요청</button>

<script>
function cancelPayments() {
    const cancelReason = document.getElementById("cancelReason").value;
    const selectedPayments = document.querySelectorAll('.payment-checkbox:checked');

    if (selectedPayments.length === 0) {
        alert('환불할 결제를 선택해주세요.');
        return;
    }

    const paymentRequests = Array.from(selectedPayments).map(checkbox => {
        const paymentKey = checkbox.closest('tr').querySelector('.payment-key').value;
        return {
            paymentKey: paymentKey,
            cancelReason: cancelReason,
            payPk: checkbox.value // payment.id를 payPk로 사용
        };
    });

    fetch('/payment/refund', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paymentRequests) // 리스트 형태로 전송
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/main'; // 성공 시 리다이렉트
        } else {
            console.error('Error canceling payments');
            // 에러 처리 추가 가능
        }
    })
    .catch(error => console.error('Fetch error:', error));
}
</script>


<!-- footer.jsp  -->
<%@include file="/WEB-INF/view/layout/footer.jsp"%>