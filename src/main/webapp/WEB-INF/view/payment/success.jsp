<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="utf-8" />
    <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>토스페이먼츠 샘플 프로젝트</title>
    <link rel="stylesheet" href="/css/payment/success.css">
</head>

<body>
<div class="container">
    <div class="success-icon">
        <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52" aria-label="결제 성공 아이콘" role="img">
            <circle class="checkmark__circle" cx="26" cy="26" r="24" fill="none"/>
            <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
        </svg>
    </div>
    <h1>
        결제가 완료되었습니다!<br>
        가게를 추가하고 UNI-ERP 서비스를 시작해보세요!
    </h1>
    <br>
    <div id="countdown">5초 후에 메인으로 이동합니다...</div>
    <br>
    <a href="/main">메인으로 이동</a>
</div>
        <script>
            let countdown = 5;
                    const countdownElement = document.getElementById("countdown");

                    const timer = setInterval(function() {
                        countdown--;
                        countdownElement.textContent = countdown + "초 후에 메인으로 이동합니다...";

                        if (countdown <= 0) {
                            clearInterval(timer);
                            window.location.href = "/main"; // 5초 후 메인 페이지로 이동
                        }
                    }, 1000); // 1초마다 실행
        </script>
</body>
