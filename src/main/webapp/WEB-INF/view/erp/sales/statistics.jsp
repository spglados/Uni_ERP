<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- 메인 컨텐츠 -->
<div class="content">

    <h1>매출 분석 통계화면 입니다.</h1>
    <p><c:out value="${salesHistory}"/></p>

</div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>