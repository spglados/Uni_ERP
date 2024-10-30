<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<style>
        .sidebar {
            width: 200px;
            float: left;
            margin-right: 20px;
            border-right: 1px solid #ccc;
            padding: 10px;
        }
        .sidebar a {
            display: block;
            margin: 10px 0;
            text-decoration: none;
            color: #333;
        }
        .sidebar a:hover {
            color: #007bff;
        }
        .profile-info {
            overflow: hidden;
        }
    </style>
</head>
    <div class="sidebar">
        <h3>내 정보</h3>
        <a href="/myPage">회원 정보 및 수정</a>
        <a href="#">가게 등록</a>
        <a href="/myPage/paymentHistory">결제 내역</a>
        <a href="#">환불 내역</a>
        <a href="#">내 문의 내역</a>
    </div>
<h1>결제 내역</h1>
<c:if test="${count != 0}">
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
                <tr>
                    <td><input type="checkbox" class="payment-checkbox" value="${payment.id}" /></td>
                    <td>${payment.orderName}</td>
                    <td>${payment.amount}</td>
                    <td>${payment.nowPayAmount}</td>
                    <td>${payment.nextPayAmount}</td>
                    <td>${payment.date}</td>
                    <td>${payment.method}</td>
                    <td>
                        <select id="cancelReason">
                            <option value="" disabled selected>취소사유</option>
                            <option value="simple">단순변심</option>
                            <option value="cancelSubscribe">가게폐점</option>
                            <option value="doublePay">중복결제</option>
                        </select>
                    </td>
                    <td>
                        <button onclick="cancelPayments(${payment.id})">환불 요청</button>
                        <input type="hidden" class="payment-key" value="${payment.paymentKey}" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${count == 0}">
    <p>결제된 내역이 없습니다.</p>
</c:if>


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