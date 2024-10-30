<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${store.name} - 세부 정보</title>
</head>
<body>
    <h1>${store.name}</h1>
    <p>운영 시간: ${store.is24Hours == 1 ? '24시간' : '일반'}</p>
    <p>상태: ${store.isOpen == 1 ? '운영중' : '휴업'}</p>
    <p>가입일: ${store.createdAt}</p>
    <a href="/admin/storeManagement">뒤로 가기</a>
</body>
</html>