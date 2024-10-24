<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오전 10:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- header.jsp  -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>


<main class="main-container">
    <div class="video-container">
        <video autoplay muted loop id="bgVideo">
            <source src="<c:url value='/videos/main.mp4'/>" type="video/mp4">
            <!-- 추가 포맷 제공 (선택 사항) -->
            Your browser does not support HTML5 video.
        </video>
        <div class="overlay-content">
            <h1>환영합니다!</h1>
            <p>이 페이지는 배경 동영상을 포함하고 있습니다.</p>
            <!-- 추가 버튼이나 링크를 여기에 배치할 수 있습니다 -->
            <a href="#learn-more" class="btn">자세히 보기</a>
        </div>
    </div>
        </main>
<script>
    // 스크롤 시 애니메이션 적용 예제
    document.addEventListener("DOMContentLoaded", function () {
        var overlay = document.querySelector('.overlay-content');

        window.addEventListener('scroll', function () {
            if (window.scrollY > 100) {
                overlay.classList.add('visible');
            } else {
                overlay.classList.remove('visible');
            }
        });
    });
</script>

<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>