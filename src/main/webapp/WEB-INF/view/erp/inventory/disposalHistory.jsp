<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 28.
  Time: 오전 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/erp/material.css">

<style>
    /* 로딩 스피너 스타일 */
    .spinner-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.7);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1050; /* 부트스트랩 모달보다 높게 설정 */
        display: none; /* 초기에는 숨김 */
    }

    /* 테이블 컨테이너의 고정 높이와 스크롤 설정 */
    .table-container {
        max-height: 600px; /* 원하는 최대 높이로 조정 */
        overflow-y: auto;
    }
</style>

<!-- 로딩 스피너 -->
<div class="spinner-overlay" id="loadingSpinner">
    <div class="spinner-border text-primary" role="status">
        <span class="sr-only">Loading...</span>
    </div>
</div>

<!-- 폐기 내역 콘텐츠 -->
<div class="content">
    <h1 class="mb-4">폐기 내역</h1>
    <hr>
    <!-- 폐기 내역 테이블 -->
    <div class="table-container">
        <table class="table table-bordered table-striped" id="historyDisposalList">
            <thead class="thead-light">
            <tr>
                <th style="background-color: #F8F399; color: black;">유형</th>
                <th style="background-color: #F8F399; color: black;">번호</th>
                <th style="background-color: #F8F399; color: black;">이름</th>
                <th style="background-color: #F8F399; color: black;">분류</th>
                <th style="background-color: #F8F399; color: black;">폐기 양</th>
                <th style="background-color: #F8F399; color: black;">폐기 날짜</th>
            </tr>
            </thead>
            <tbody>
            <!-- 서버에서 받아온 폐기 내역이 동적으로 삽입됩니다 -->
            </tbody>
        </table>
    </div>
</div>

<!-- JavaScript 섹션 -->
<script>
    // 폐기 내역을 JavaScript 배열로 정의 (서버에서 직접 작성할 예정)
    const disposalHistoryList = [
        <%-- 서버에서 폐기 내역 데이터를 JSON 형태로 전달 --%>
        <c:forEach var="history" items="${disposalHistoryList}" varStatus="status">
        {
            type: '${history.type}',
            code: '${history.code}',
            name: '${history.name}',
            category: '${history.category}',
            amount: '${history.amount}',
            disposalDate: '${history.disposalDate}'
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    console.log('disposalHistoryList', disposalHistoryList);

    document.addEventListener('DOMContentLoaded', function () {
        // 필터 및 검색 요소 선택
        const historyMaterialCategoryFilter = document.getElementById('historyMaterialCategoryFilter');
        const historyProductCategoryFilter = document.getElementById('historyProductCategoryFilter');
        const historySearchInput = document.getElementById('historySearchInput');
        const historyDisposalList = document.getElementById('historyDisposalList').getElementsByTagName('tbody')[0];
        const loadingSpinner = document.getElementById('loadingSpinner');

        // 폐기 내역 테이블에 데이터를 삽입하는 함수
        function populateHistoryDisposalTables(data) {
            historyDisposalList.innerHTML = ''; // 기존 내용 초기화

            data.forEach(function (history) {
                let row = '<tr>' +
                    '<td>' + history.type + '</td>' +
                    '<td>' + history.code + '</td>' +
                    '<td>' + history.name + '</td>' +
                    '<td>' + history.category + '</td>' +
                    '<td>' + history.disposalAmount + ' ' + history.unit + '</td>' +
                    '<td>' + history.disposalDate + '</td>' +
                    '</tr>';
                historyDisposalList.innerHTML += row;
            });
        }

        // 초기 데이터 로딩 함수 (서버로부터 데이터 fetch)
        function loadDisposalHistory() {
            // 로딩 스피너 표시
            loadingSpinner.style.display = 'flex';

            // 서버로부터 폐기 내역 데이터를 fetch (예: AJAX 요청)
            fetch('/erp/inventory/month-adjustment', {
                method: 'POST'
            }) // 실제 데이터 조회를 위한 서버 엔드포인트로 변경 필요
                .then(response => {
                    if (!response.ok) {
                        throw new Error('네트워크 응답이 올바르지 않습니다.');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('data', data);
                    // 데이터 삽입
                    populateHistoryDisposalTables(data);
                    // 로딩 스피너 숨김
                    loadingSpinner.style.display = 'none';
                })
                .catch(error => {
                    // 오류 처리
                    console.error('오류:', error);
                    alert('폐기 내역을 불러오는 중 오류가 발생했습니다.');
                    // 로딩 스피너 숨김
                    loadingSpinner.style.display = 'none';
                });
        }

        // 초기 데이터 로딩
        loadDisposalHistory();
    });
</script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
