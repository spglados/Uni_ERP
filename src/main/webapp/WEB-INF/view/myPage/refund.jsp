<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>
</head>
<body>

    <table>
        <thead>
            <tr>
                <th></th>
                <th>상품 이름</th>
                <th>상품 원가</th>
                <th>결제된 금액</th>
                <th>다음 달 결제 예정 금액</th>
                <th>결제된 날짜</th>
                <th>결제 수단</th>
                <th>취소 사유</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="payment" items="${payments}">
                <c:if test="${not empty payment.cancelReason}">
                    <tr>
                        <td><input type="radio" class="payment-radio" name="selectedPayment" value="${payment.id}" /></td>
                        <td>${payment.orderName}</td>
                        <td>${payment.amount}</td>
                        <td>${payment.nowPayAmount}</td>
                        <td>${payment.nextPayAmount}</td>
                        <td>${payment.date}</td>
                        <td>${payment.method}</td>
                        <td>
                            <c:choose>
                                <c:when test="${refund.cancelReason == 'simple'}">단순변심</c:when>
                                <c:when test="${refund.cancelReason == 'cancelSubscribe'}">가게폐점</c:when>
                                <c:when test="${refund.cancelReason == 'doublePay'}">중복결제</c:when>
                                <c:otherwise>기타</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${payment.cancel != 'Y'}">
                                <button onclick="cancelPayments('${payment.cancelReason}', '${payment.id}')">환불 승인</button>
                            </c:if>
                            <c:if test="${payment.cancel == 'Y'}">
                                <button disabled>승인 완료</button>
                            </c:if>
                            <input type="hidden" class="payment-key" value="${payment.paymentKey}" />
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </tbody>
    </table>

    <script>
    function cancelPayments(cancelReason) {
        const selectedPayment = document.querySelector('.payment-radio:checked');

        if (!selectedPayment) {
            alert('환불할 결제를 선택해주세요.');
            return;
        }

        const paymentKey = selectedPayment.closest('tr').querySelector('.payment-key').value;
        const paymentRequest = {
            paymentKey: paymentKey,
            cancelReason: cancelReason,
            payPk: selectedPayment.value // payment.id를 payPk로 사용
        };

        // 요청이 배열 형식으로 전송되도록 만듭니다.
        const paymentRequests = [paymentRequest];

        fetch('/payment/refund', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paymentRequests) // 배열로 전송
        })
        .then(response => {
            if (response.ok) {
                alert('승인 완료');
                location.reload(); // 페이지를 리로드합니다.
            } else {
                console.error('Error canceling payments');
            }
        })
        .catch(error => console.error('Fetch error:', error));
    }
    </script>

    <!-- footer.jsp  -->
    <%@include file="/WEB-INF/view/layout/footer.jsp"%>
</body>
</html>
