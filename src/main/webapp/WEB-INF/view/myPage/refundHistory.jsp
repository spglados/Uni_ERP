<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/myPageHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<main class="main-container">
    <h1>환불 내역</h1>
    <div class="profile-info">

    <c:if test="${empty refund}">
        <p class="no-refund">환불 내역이 없습니다.</p>
    </c:if>

    <c:if test="${not empty refund}">
        <table>
            <thead>
                <tr>
                    <th>환불 요청 ID</th>
                    <th>환불 금액</th>
                    <th>환불 사유</th>
                    <th>환불 승인 날짜</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="refund" items="${refund}">
                    <tr>
                        <td>${refund.id}</td> <!-- refund 객체의 ID -->
                        <td>${refund.cancelAmount}</td> <!-- refund 객체의 ID -->
                        <td>
                                    <c:choose>
                                        <c:when test="${refund.cancelReason == 'simple'}">단순변심</c:when>
                                        <c:when test="${refund.cancelReason == 'cancelSubscribe'}">가게폐점</c:when>
                                        <c:when test="${refund.cancelReason == 'doublePay'}">중복결제</c:when>
                                        <c:otherwise>기타</c:otherwise>
                                    </c:choose>
                                </td>
                        <td id="date-${refund.id}"> <!-- 날짜 출력 ID 설정 -->
                                                    ${refund.approvedAt} <!-- 원본 날짜 문자열 출력 -->
                                                </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

        </div>
        </main>
<script>
        document.addEventListener("DOMContentLoaded", function() {
            const refundRows = document.querySelectorAll("tbody tr");
            refundRows.forEach(row => {
                const dateCell = row.querySelector("td[id^='date-']");
                const dateString = dateCell.innerText; // 승인 날짜 문자열 가져오기
                const dateParts = dateString.split("T"); // T 기준으로 분리
                dateCell.innerText = dateParts[0]; // 날짜 부분만 출력
            });
        });
    </script>

<%@ include file="/WEB-INF/view/layout/myPageFooter.jsp" %>