<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>


<!--시재점검-->
<button onclick="inspection()">시재점검</button>
<br>

<!--금고관리-->
<button onclick="safe()">금고관리</button>
<br><br><br><br><br>


<!--오픈마감-->
<button onclick="openAndClosed()">오픈/마감</button>

<script>
function inspection() {
    window.open('http://localhost:8080/inspection', '_blank', 'width=800,height=600');
}

function safe() {
    window.open('http://localhost:8080/safe', '_blank', 'width=800,height=600');
}

function openAndClosed() {
    window.open('http://localhost:8080/openAndClosed', '_blank', 'width=800,height=600');
}


</script>

<!-- footer.jsp  -->
<%@include file="/WEB-INF/view/layout/footer.jsp"%>