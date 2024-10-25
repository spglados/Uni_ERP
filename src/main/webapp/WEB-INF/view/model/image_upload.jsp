<%--
  Created by IntelliJ IDEA.
  User: SPGLADOS
  Date: 24. 10. 13.
  Time: 오후 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>

<main>

  <h1>파일 업로드</h1>
  <form action="/test/upload" method="post" class="advertise" enctype="multipart/form-data" style="border: 1px solid #ccc;">
    <div class="form-group">
      <label for="file" class="file-input">이미지 파일</label>
      <input type="file" id="file" name="file" class="file-input">
    </div>
    <button type="submit">텍스트추출</button>
  </form>

</main>


<%@include file="/WEB-INF/view/layout/footer.jsp"%>