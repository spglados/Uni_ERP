<%--
  Created by IntelliJ IDEA.
  User: KDP
  Date: 24. 10. 10.
  Time: 오후 5:03
  To change this template use File | Settings | File Templates.
--%>

<!-- header.jsp -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<main class="main-container">

  <h1>직원 목록</h1>

  <!-- 직원 목록 테이블 -->
  <table class="employee-table">
    <thead>
      <tr>
        <th>이름</th>
        <th>부서</th>
        <th>직책</th>
        <th>연락처</th>
        <th>액션</th>
      </tr>
    </thead>
    <tbody>
      <%-- 직원 목록을 루프를 통해 출력 --%>
      <c:forEach var="employee" items="${employeeList}">
        <tr>
          <td>${employee.name}</td>
          <td>${employee.department}</td>
          <td>${employee.position}</td>
          <td>${employee.contact}</td>
          <td>
            <a href="editEmployee.do?id=${employee.id}" class="btn-edit">수정</a>
            <a href="deleteEmployee.do?id=${employee.id}" class="btn-delete"
               onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <!-- 직원 등록 버튼 -->
  <div class="employee-register">
    <a href="registerEmployee.do" class="btn-register">직원 등록</a>
  </div>

</main>

<!-- footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>

