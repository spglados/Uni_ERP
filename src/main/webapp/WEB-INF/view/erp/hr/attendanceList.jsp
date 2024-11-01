<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<div id="myGrid" style="width: 100%" class="ag-theme-quartz"></div>

<script type="application/javascript">
    const attendanceList = JSON.parse('${dataList}');
    console.log('dataList', attendanceList);
    console.log('typeof(dataList)', typeof(attendanceList));
</script>
<%-- ag-grid 랜더링 스크립트 --%>
<script src="/js/erp/hr/attendance.js"></script>

<div id="toastContainer" class="position-fixed bottom-0 right-0 p-3" style="z-index: 1060;">
    <!-- JavaScript를 통해 토스트가 여기에 동적으로 추가됩니다 -->
</div>


<!-- Toast 및 로딩 스피너를 위한 JavaScript 추가 -->
<script src="/js/toastHelper.js"></script>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>