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
<style>
    .register-container {
        display: flex;
        width: 100%;
        height: 100%;
    }
</style>

<!-- 냉동품 컨텐츠 -->
<div class="content">
    <h1>재고 등록</h1>
    <hr>
    <!-- 자재 필터 및 등록 버튼 영역 -->

    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2 id="categoryTitle">전체</h2>
            </div>
            <div class="d-flex justify-content-between">
                <button class="btn btn-outline-info rounded-pill" data-toggle="modal" data-target="#registerModal"
                        style="margin-right: 20px;">등록
                </button>
            </div>
        </div>
    <!-- 자재 등록 폼 -->
    <form id="materialForm" action="/material/save" method="post">
        <div class="register-container">
            <!-- 기본 정보 섹션 -->
            <div class="form-section">
                <h3>기본 정보</h3>
                <div class="form-row">
                    <!-- 이름 -->
                    <div class="form-group col-md-6">
                        <label for="name">이름 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <!-- 자재 코드 -->
                    <div class="form-group col-md-6">
                        <label for="materialCode">자재 코드 <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="materialCode" name="materialCode" required>
                    </div>
                </div>
                <div class="form-row">
                    <!-- 카테고리 -->
                    <div class="form-group col-md-6">
                        <label for="category">카테고리</label>
                        <input type="text" class="form-control" id="category" name="category">
                    </div>
                    <!-- 단위 -->
                    <div class="form-group col-md-6">
                        <label for="unit">단위 <span class="text-danger">*</span></label>
                        <select class="form-control" id="unit" name="unit" required>
                            <option value="">선택하세요</option>
                            <c:forEach var="unitCategory" items="${unitCategories}">
                                <option value="${unitCategory}">${unitCategory}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <!-- 추가 정보 섹션 -->
            <div class="form-section">
                <h3>추가 정보</h3>
                <div class="form-row">
                    <!-- 서브 수량 -->
                    <div class="form-group col-md-6">
                        <label for="subAmount">서브 수량</label>
                        <input type="number" step="0.01" class="form-control" id="subAmount" name="subAmount">
                    </div>
                    <!-- 서브 단위 -->
                    <div class="form-group col-md-6">
                        <label for="subUnit">서브 단위</label>
                        <select class="form-control" id="subUnit" name="subUnit">
                            <option value="">선택하세요</option>
                            <c:forEach var="subUnitCategory" items="${unitCategories}">
                                <option value="${subUnitCategory}">${subUnitCategory}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <!-- 입고 날짜 -->
                    <div class="form-group col-md-6">
                        <label for="enterDate">입고 날짜</label>
                        <input type="date" class="form-control" id="enterDate" name="enterDate" value="${currentDate}">
                    </div>
                    <!-- 알람 주기 -->
                    <div class="form-group col-md-6">
                        <label for="alarmCycle">알람 주기</label>
                        <input type="number" step="0.01" class="form-control" id="alarmCycle" name="alarmCycle">
                    </div>
                </div>
                <div class="form-row">
                    <!-- 알람 단위 -->
                    <div class="form-group col-md-6">
                        <label for="alarmUnit">알람 단위</label>
                        <select class="form-control" id="alarmUnit" name="alarmUnit">
                            <option value="">선택하세요</option>
                            <c:forEach var="alarmUnitCategory" items="${unitCategories}">
                                <option value="${alarmUnitCategory}">${alarmUnitCategory}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </form>

    </div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>