<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>


<!--시재점검-->
<button onclick="inspection()">시재점검</button>
<br>

<!--금고관리-->
<button >금고관리</button>
<br><br><br><br><br>


<!--오픈마감-->
<button >오픈하기</button>
<br>
<button >마감하기</button>
 <label><input type="checkbox" name="option1" value="">24시간</label>



<script>
function inspection() {
    window.open('http://localhost:8080/inspection', '_blank', 'width=800,height=600');
}


</script>

<!-- footer.jsp  -->
<%@include file="/WEB-INF/view/layout/footer.jsp"%>