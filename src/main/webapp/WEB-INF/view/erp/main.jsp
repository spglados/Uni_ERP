<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/styles/ag-grid.css">
<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/styles/ag-theme-alpine.css">
<!-- 선택 사항: 부트스트랩 포함 (스타일링용) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">


<!-- 메인 컨텐츠 -->
<div class="content">
    <header>
        <h1>ERP 메인 페이지</h1>
    </header>
    <c:if test="${storeList != null}">
        <div class="user-information">
            <div class="name-welcome">최예나님 환영합니다!</div>
            <label for="storeId">가게선택</label>
            <select name="store" id="storeId">
                <c:forEach var="store" items="${storeList}">
                    <option value="${store.id}" <c:if test="${storeId == store.id}">selected</c:if> >${store.id} - ${store.name}</option>
                </c:forEach>
            </select>
        </div>
          <!-- 알림 Toast 추가 -->
        <div class="toast-container">
            <div id="welcomeToast" class="toast align-items-center text-bg-primary" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                        최예나님,
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>
</div>


<!-- 선택 사항: AJAX 요청을 위한 Axios 포함 -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/statistics.js"></script>
<script>
    $(document).ready(function () {
        const toastElement = document.getElementById('welcomeToast');
        const toast = new bootstrap.Toast(toastElement, { autohide: false });
        toast.show();

        // 5초 후에 자동으로 Toast 닫기
        setTimeout(() => {
            toast.hide();
        }, 5000);
    });

    document.addEventListener('DOMContentLoaded', function () {
        const storeSelect = document.getElementById('storeId');

        storeSelect.addEventListener('change', function () {
            const selectedValue = storeSelect.value;

            // 이곳에 fetch를 사용하여 서버로 데이터를 보내는 코드를 작성하세요.

            fetch('/erp/store/' + selectedValue, {
                method: "PUT"
            })
                .then(response => {
                    if(response.ok) {
                        window.location.reload();
                    }
                }).catch(error => {
                    console.log('error', error);
                    alert('가게 변경에 실패했습니다.');
            });

            console.log(`Selected Store ID: ${selectedValue}`);
        });
    });
</script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
