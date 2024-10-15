<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <!-- 숨겨진 입력 필드 추가 -->
    <form id="paymentForm" action="/payment/success" method="get">
        <input type="hidden" name="desiredPayDate" id="desiredPayDate" value="" />
        <a href="#" onclick="submitForm()">메인으로 이동</a>
    </form>

    <script>
        function submitForm() {
            const desiredDay = sessionStorage.getItem('desiredDay'); // 세션 스토리지에서 가져오기
            if (desiredDay) {
                document.getElementById('desiredPayDate').value = desiredDay; // 숨겨진 필드에 값 설정
            }
            document.getElementById('paymentForm').submit(); // 폼 제출
        }
    </script>
</body>
</html>
