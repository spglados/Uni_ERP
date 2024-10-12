<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/erp/product.css">

<!-- 메인 컨텐츠 -->
<div class="content">

  <h1>상품 등록</h1>
  <hr>
  <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
    <button class="btn btn-outline-info rounded-pill" style="margin-right: 20px;">등록</button>
    <select class="form-control select" style="margin-right: 30px;">
        <option value="">메인</option>
        <option value="">사이드</option>
        <option value="">주류</option>
      </select>
    </div>

  <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
    <h2>메인</h2>
    <hr>

</div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>