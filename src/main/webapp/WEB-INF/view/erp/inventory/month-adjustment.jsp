<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">
<!-- Font Awesome (integrity 속성 제거) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
      crossorigin="anonymous" referrerpolicy="no-referrer"/>

<!-- 재고 관리 콘텐츠 -->
<div class="content">
    <h1>월 재고 현황</h1>
    <hr>
    <!-- 자재 필터 및 저장 버튼 영역 -->
    <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
        <!-- 카테고리 선택 필터 -->
        <select id="categoryFilter" class="form-control select" style="margin-right: 30px;">
            <option value="전체">전체</option>
            <option value="냉동품">냉동품</option>
            <option value="냉장품">냉장품</option>
            <option value="상온품">상온품</option>
        </select>
    </div>

    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2 id="categoryTitle">전체</h2>
            </div>
            <div class="d-flex justify-content-between">
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- 자재 목록 테이블 -->
            <table class="table table-bordered table-striped" id="materialList">
                <thead class="thead-dark">
                <tr>
                    <th>자재코드</th>
                    <th>자재명</th>
                    <th>금월 입고량</th>
                    <th>금월 사용량</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="material" items="${materialStatusList}" varStatus="status">
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
    </script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
