<%--
        Created by IntelliJ IDEA.
        User: namch
        Date: 24. 10. 10.
        Time: 오후 7:53
        To change this template use File | Settings | File Templates.
        --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/notice.css">
<!-- Font Awesome for icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<!-- AOS CSS -->
<link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet">

<main class="main-container">
    <section class="notice-detail-section">
        <div class="notice-detail-container">
            <div class="notice-header">
                <h1 class="notice-title">${notice.title}</h1>
                <div class="notice-meta">
                    <span class="notice-category">${notice.category}</span>
                    <span class="notice-date"><i class="fas fa-calendar-alt"></i> ${notice.dateFormatter()}</span>
                    <span class="notice-views"><i class="fas fa-eye"></i> ${notice.views}</span>
                </div>
            </div>
            <div class="notice-content">
                <p>${notice.content}</p>
            </div>
            <div class="notice-actions">
                <a href="${pageContext.request.contextPath}/notice" class="btn btn-secondary" aria-label="목록으로 돌아가기"><i class="fas fa-arrow-left"></i> 목록으로 돌아가기</a>
            </div>
        </div>
    </section>
</main>

<!-- AOS JS -->
<script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
<script>
    AOS.init({
        duration: 1000, // 애니메이션 지속 시간 (ms)
        once: true,     // 한 번만 애니메이션 실행
    });
</script>

<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
