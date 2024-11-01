<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${store.name} - 세부 정보</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1 class="mt-4">${store.name}</h1>
                <p class="lead">운영 시간: ${store.is24Hours == 1 ? '24시간' : '일반'}</p>
                <p class="lead">상태: ${store.isOpen == 1 ? '운영중' : '휴업'}</p>
                <p class="lead">가입일: ${store.createdAt}</p>
                <button type="button" class="btn btn-secondary" onclick="window.close()">닫기</button>
            </div>
        </div>
    </div>
</body>
</html>