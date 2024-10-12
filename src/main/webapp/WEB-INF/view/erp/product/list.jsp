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

        <!-- 그리드 레이아웃 박스 -->
        <div class="container-fluid mt-4">
            <div class="row d-flex flex-wrap justify-content-start">
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 1</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 2</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 3</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 4</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 5</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
                <div class="custom-col mb-4">
                    <div class="box bg-info text-center p-4">테이블 6</div>
                </div>
            </div>
        </div>

    </div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>