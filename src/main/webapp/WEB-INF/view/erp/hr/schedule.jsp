<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/hr/calendar.css">
<link href='https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.13.1/css/all.css' rel='stylesheet'>
<div class="p-1 w-100" id='calendar'></div>
<script type="module">
    import { initializeCalendar } from '/js/erp/hr/calendarModule.js';

    document.addEventListener('DOMContentLoaded', function() {
        const calendarEl = document.getElementById('calendar');
        initializeCalendar(calendarEl);
    });

</script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>