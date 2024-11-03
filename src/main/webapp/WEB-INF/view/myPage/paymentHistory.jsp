<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/myPageHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<main class="main-container">
<h1>결제 내역</h1>
    <div class="profile-info">
    <c:if test="${not empty payments}">
    <table>
        <thead>
            <tr>
                <th>선택</th>
                <th>상품 이름</th>
                <th>상품 원가</th>
                <th>결제된 금액</th>
                <th>다음 달 결제 예정 금액</th>
                <th>결제된 날짜</th>
                <th>결제 수단</th>
                <th>취소 사유</th>
                <th>환불 요청</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="payment" items="${payments}">
                <tr onclick="selectPayment('${payment.id}', this)">
                    <td><input type="radio" name="paymentId" value="${payment.id}" /></td>
                    <td>${payment.orderName}</td>
                    <td>${payment.amount}</td>
                    <td>${payment.nowPayAmount}</td>
                    <td>${payment.nextPayAmount}</td>
                    <td>${payment.date}</td>
                    <td>${payment.method}</td>
                    <td>
                        <select class="cancel-reason" <c:if test="${not empty payment.cancelReason}">disabled</c:if>>
                            <option value="" disabled selected>취소사유</option>
                            <option value="simple">단순변심</option>
                            <option value="cancelSubscribe">가게폐점</option>
                            <option value="doublePay">중복결제</option>
                        </select>
                    </td>
                    <td>
                        <c:if test="${not empty payment.cancelReason}">
                            <c:if test="${payment.cancel == 'N'}">
                                <button disabled>환불 요청중</button>
                            </c:if>
                            <c:if test="${payment.cancel == 'Y'}">
                                <button disabled>승인 완료</button>
                            </c:if>
                        </c:if>
                        <c:if test="${empty payment.cancelReason}">
                        <button onclick="cancelPayment()">환불 요청하기</button>
                        </c:if>
                        <input type="hidden" class="payment-key" value="${payment.paymentKey}" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    </c:if>



<c:if test="${empty payments}">
    <p>결제된 내역이 없습니다.</p>
</c:if>
        </div>
</main>
<script>

const paymentCount = ${paymentCount}; // paymentCount 값
const storeCount = ${storeCount}; // storeCount 값


function selectPayment(paymentId, row) {
    // 선택된 항목 강조
    const rows = document.querySelectorAll('tbody tr');
    rows.forEach(r => r.classList.remove('selected'));
    row.classList.add('selected');
}

function cancelPayment() {
    const selectedRadio = document.querySelector('input[name="paymentId"]:checked');
    const selectedReason = document.querySelector('select.cancel-reason option:checked');

    if (!selectedRadio) {
        alert("환불 요청할 결제를 선택해 주세요.");
        return;
    }

    if (paymentCount <= storeCount) {
            alert("먼저 가게를 삭제해 주세요.");
            return;
    }

    const paymentId = selectedRadio.value;
    const cancelReasonSelect = selectedRadio.closest('tr').querySelector('select.cancel-reason');
    const cancelReason = cancelReasonSelect.value;

    if (!cancelReason) {
        alert("취소 사유를 선택해 주세요.");
        return;
    }

    // POST 요청을 위한 fetch
    fetch('/myPage/cancelPayment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            paymentId: paymentId,
            cancelReason: cancelReason
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("환불 요청이 성공적으로 전송되었습니다.");
            window.location.reload(); // 페이지 새로 고침
        } else {
            alert("환불 요청에 실패했습니다: " + data.message);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("요청 중 오류가 발생했습니다.");
    });
}
</script>

<%@ include file="/WEB-INF/view/layout/myPageFooter.jsp" %>
