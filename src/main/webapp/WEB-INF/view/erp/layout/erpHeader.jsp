<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>UNI-ERP</title>
    <link rel="stylesheet" href="/css/erp/erpMain.css">
    <!-- 수정된 Font Awesome 링크 (integrity 속성 제거) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
</head>
<body>
<div class="erp-container">
    <!-- 사이드바 메뉴 -->
    <nav class="sidebar">
        <a href="/erp/main"><img src="/images/logo/logo_clear.png" class="animate__animated animate__fadeIn" alt="메인로고" style="height: 100px; width: 125px;"></a>
        <ul class="sidebar-menu">
            <li>
                <a href="javascript:void(0);" class="hr-toggle">인사 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/erp/hr/employee-register">직원 등록</a></li>
                    <li><a href="/erp/hr/employee-list">직원 목록</a></li>
                    <li><a href="/erp/hr/schedule">근무 일정</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="inventory-toggle">재고 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/erp/inventory/receiving">입고 관리</a></li>
                    <li><a href="/erp/inventory/registration">재고 등록</a></li>
                    <li><a href="/erp/inventory/situation">재고 현황</a></li>
                    <li><a href="/erp/inventory/status">재고 관리</a></li>
                    <li><a href="/erp/inventory/day-adjustment">일 재고 현황</a></li>
                    <li><a href="/erp/inventory/month-adjustment">월 재고 현황</a></li>
                    <li><a href="/erp/inventory/disposal">폐기 관리</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="sales-toggle">매출 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/erp/sales/record">매출 입력</a></li>
                    <li><a href="/erp/sales/history">매출 기록</a></li>
                    <li><a href="/erp/sales/statistics">매출 통계</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="product-toggle">상품 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/erp/product/registration">상품 등록</a></li>
                    <li><a href="/erp/product/list">상품 목록</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="product-toggle">가상 POS ▼</a>
                <ul class="sub-menu">
                    <li><a href="/erp/pos/main" target='_blank'>POS 실행</a></li>
                </ul>
            </li>
        </ul>
    </nav>