<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>입고 관리</h1>
    <hr>

    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div class="d-flex justify-content-between">
                <select id="categoryFilter" class="form-control select" style="margin-right: 30px;">
                    <option value="전체">전체</option>
                    <option value="상품명">상품명</option>
                    <option value="가격">가격</option>
                    <option value="입고량">입고량</option>
                    <option value="공급처">공급처</option>
                    <option value="유통기한">유통기한</option>
                    <option value="입고 날짜">입고 날짜</option>
                    <option value="등록 날짜">등록 날짜</option>
                </select>
                <input id="searchInput" placeholder="내역 검색" class="form-control mr-2" style="width: 200px;">
            </div>
            <div>
                <button>입고 등록</button>
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- 자재 목록 테이블 -->
            <table class="table table-bordered table-striped" id="materialList">
                <thead class="thead-dark">
                <tr>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>입고량</th>
                    <th>공급처</th>
                    <th>유통기한</th>
                    <th>입고 날짜</th>
                    <th>등록 날짜</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="materialOrder" items="${materialOrderList}">
                    <tr>
                        <th>${materialOrder.name}</th>
                        <th>${materialOrder.price}원</th>
                        <th>${materialOrder.amount}&nbsp;${materialOrder.unit}</th>
                        <th>${materialOrder.supplier}</th>
                        <th>${materialOrder.expirationDate}</th>
                        <th>${materialOrder.receiptDate}</th>
                        <th>${materialOrder.enterDate}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>