// 이메일 관련 기능
document.addEventListener("DOMContentLoaded", function() {
    const sendEmailBtn = document.getElementById('send--email--verification'); // 이메일 전송 버튼

    sendEmailBtn.addEventListener('click', function() {
        const email = document.getElementById('email').value
        if (!validateEmail(email)) {
            alert("사용할 수 없는 이메일 형식");
            return;
        }
        sendEmailBtn.innerText = "전송중";
        sendEmailBtn.disabled = true;
        // 이메일 인증 요청
        fetch(`/email/sendVerification?email=${encodeURIComponent(email)}`, {
            method: 'POST'
        })
            .then(response => response.text())
            .then(data => {
                // 카운트다운 시작
                startCountdown();
            })
            .catch(error => {
                console.error('Error:', error);
                sendEmailBtn.disabled = false;
            });
    });

    // 카운트다운 시작 함수
    function startCountdown() {
        sendEmailBtn.innerText = "대기중";
        let countdown = 300; // 5분 (300초)
        const statusElement = document.getElementById('email--verification--status');
        statusElement.innerText = "5분 내에 인증을 완료해주세요.";

        const interval = setInterval(function() {
            countdown--;
            statusElement.innerText = `남은 시간: ${countdown}초`;

            if (countdown <= 0) {
                clearInterval(interval);
                statusElement.innerText = "인증 시간이 만료되었습니다.";
                sendEmailBtn.innerText = "재전송";
                sendEmailBtn.disabled = false;
            }
        }, 1000);

        // 웹소켓을 통해 인증 결과 대기
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/topic/verify', function(message) {
                if (message.body === 'ok') {
                console.log("성공");
                    clearInterval(interval); // 카운트다운 중지
                    statusElement.innerText = "이메일 인증이 완료되었습니다.";
                    sendEmailBtn.innerText = "완료";
                    sendEmailBtn.disabled = true; // 버튼 비활성화
                    alert('이메일 인증 성공!');
                    // TODO 인증 완료시 boolean 변수 처리
                    isEmailChecked = true;
                } else {
                    statusElement.innerText = "인증 실패. 다시 시도해주세요.";
                    sendEmailBtn.disabled = false;
                }
            });
        });
    }
});

// Email validation helper function
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}
