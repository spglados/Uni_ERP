<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>토스페이먼츠 샘플 프로젝트</title>
</head>

<body>
       <img width="100px" src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" />
       <h2>결제를 완료했어요</h2>
       ${response}
       <a href="/main">메인으로 이동</a>
</body>


<script>
    document.addEventListener("DOMContentLoaded", function() {
        const desiredDay = sessionStorage.getItem('desiredDay'); // 세션 스토리지에서 가져오기
        if (desiredDay) {
            fetch('/payment/setDesiredDay', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ desiredDay: desiredDay })
            });
        }
    });
</script>

</html>
