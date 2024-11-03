<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/myPageHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<main class="main-container">
    <h1>내 문의 내역</h1>
    <div class="profile-info">
    <c:if test="${empty contact}">
                <div class="no-refund">문의하신 내역이 없습니다.</div><br>
    </c:if>
    <button onclick="support()">문의하러가기</button><br><br>
        <c:if test="${not empty contact}">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 50px;">번호</th> <!-- 너비 조정 -->
                        <th>문의 제목</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="contact" items="${contact}" varStatus="status">
                        <tr>
                            <td style="width: 50px;">${status.index + 1}</td> <!-- 번호 칸 너비 -->
                            <td>
                                <a href="javascript:void(0);" onclick="openContactDetail(${contact.id})">${contact.title}</a>
                            </td>
                            <td>${contact.status.name() == 'OPEN' ? '답변 미완료' : '답변 완료'}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

    </div>
</main>

<script>
function openContactDetail(id) {
    const url = '/my-page/contact-detail/' + id;
    console.log(url); // 생성된 URL 확인
    window.open(url, '_blank', 'width=800,height=600');
}

function support() {
    window.location.href = '/support';
}
</script>

<%@ include file="/WEB-INF/view/layout/myPageFooter.jsp" %>
