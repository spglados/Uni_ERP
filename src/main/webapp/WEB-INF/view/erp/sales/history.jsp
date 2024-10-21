<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!-- 메인 컨텐츠 -->
<div class="content">

        <div class="text-center">
            <h5>매출(연누계)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span class="sales-year-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                    <span class="sales-year-percent-span" style="font-size: 15px; color: green;"></span>
                    <span style="font-size: 15px; color: gray;">전년 대비</span>
                </div>
            </div>
        </div>

        <div class="text-center">
            <h5>매출이익(연누계)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span class="sales-profit-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                    <span class="sales-profit-percent-span" style="font-size: 15px; color: green;"></span>
                    <span style="font-size: 15px; color: gray;">전년 대비</span>
                </div>
            </div>
        </div>

        <div class="text-center">
            <h5>목표달성률(연)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span class="target-achievement-rate-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
            </div>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>상품명</th>
                    <th>매출(월)</th>
                    <th>전월동기매출</th>
                    <th>성장율(%)</th>
                    <th>매출(연누계)</th>
                    <th>전년동기매출</th>
                    <th>성장율(%)</th>
                    <th>매출이익(₩)</th>
                    <th>비고</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/erp/sales/detail.js"></script>
<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
