<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/myPageHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<main class="main-container">
    <h1>가게 등록</h1>
    <div class="profile-info">

    <c:if test="${empty store and count == 0}">
        <p class="no-refund">보유중인 가게 없습니다.</p>
        <p>* 가게를 등록하시려면 결제를 먼저 진행해주세요. *</p>
        <button onclick="payment()">결제하러가기</button>
    </c:if>


    <c:if test="${not empty store}">
    <p>현재 보유중인 가게 수: ${storeCount} &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 등록가능한 가게 수: ${count - storeCount}</p>

    <c:if test="${count - storeCount == 0}">
        <p class="no-refund">더 이상 가게를 등록할 수 없습니다.</p>
        <p>가게를 등록하시려면 결제를 진행해주세요. <button onclick="payment()">결제하러가기</button></p>
    </c:if>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>이름</th>
                <th>24시간 운영</th>
                <th>운영 상태</th>
                <th>주소</th>
                <th>등록일</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="store" items="${store}">
                <tr>
                    <td>${store.id}</td>
                    <td>${store.name}</td>
                    <td>${store.is24Hours == 1 ? '예' : '아니오'}</td>
                    <td>${store.isOpen == 1 ? '열림' : '닫힘'}</td>
                    <td>${store.storeAddress}</td>
                    <td><fmt:formatDate value="${store.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>
                        <button onclick="deleteStore(${store.id})">가게 삭제</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    </c:if>
    <c:if test="${storeCount < count}">
            <br><button onclick="saveStore()">가게 등록</button>
        </c:if>
        </div>
</main>

<script>
    function saveStore() {
        window.open('http://localhost:8080/myPage/saveStore', '_blank', 'width=800,height=600');
    }
    function payment() {
        window.location.href = '/payment';
    }

   function deleteStore(storeId) {
       if (confirm("정말 이 가게를 삭제하시겠습니까?")) {
           fetch(`/myPage/deleteStore/${storeId}`, {
               method: 'POST', // POST 메서드 사용
               headers: {
                   'Content-Type': 'application/json',
               },
               body: JSON.stringify({ storeId: storeId })
           })
           .then(response => {
               if (response.ok) {
                   alert("가게가 삭제되었습니다.");
                   window.location.reload();
               } else {
                   alert("가게 삭제에 실패했습니다.");
               }
           })
           .catch(error => {
               console.error("Error:", error);
               alert("삭제 요청 중 오류가 발생했습니다.");
           });
       }
   }
</script>

<%@ include file="/WEB-INF/view/layout/myPageFooter.jsp" %>