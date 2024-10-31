<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="utf-8" />
    <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>토스페이먼츠 샘플 프로젝트</title>
</head>

<body>
    <img width="100px" src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" />
    <h2>결제를 완료했어요</h2>
    <div id="countdown">3초 후에 메인으로 이동합니다...</div>
    <a href="/main">메인으로 이동</a>
    <script>
        let countdown = 3;
                const countdownElement = document.getElementById("countdown");

                const timer = setInterval(function() {
                    countdown--;
                    countdownElement.textContent = countdown + "초 후에 메인으로 이동합니다...";

                    if (countdown <= 0) {
                        clearInterval(timer);
                        window.location.href = "/main"; // 3초 후 메인 페이지로 이동
                    }
                }, 1000); // 1초마다 실행
    </script>
</body>
</html>
