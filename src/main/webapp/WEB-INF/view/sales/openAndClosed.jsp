<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp -->

<!-- 오픈마감 -->
<button id="openButton" onclick="confirmOpen()">오픈하기</button>
<br>
<button id="closeButton" onclick="closeBusiness()">마감하기</button>
<label style="color: black;">
    <input type="checkbox" name="option1" value="">24시간
</label>

<br><br><br><br><br>
<button id="payButton" onclick="pay()" ${status == 1 ? '' : 'disabled'}>결제하기</button>

<script>
    // 오픈하기 버튼 클릭 시 확인창 표시
    function confirmOpen() {
        if (confirm("오픈하시겠습니까?")) {
            fetch('/open', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({}) // 필요시 추가 데이터 전송
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                alert(data); // 성공 메시지 알림
                location.reload(); // 페이지 리로드하여 상태 업데이트
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }
    }

    // 마감하기 버튼 클릭 시 상태 변경
    function closeBusiness() {
        if (confirm("마감하시겠습니까?")) {
            fetch('/close', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({}) // 필요시 추가 데이터 전송
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                alert(data); // 성공 메시지 알림
                location.reload(); // 페이지 리로드하여 상태 업데이트
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }
    }

    // 결제하기 버튼 클릭 시 실행되는 함수
    function pay() {
        // 결제 로직 추가
    }
</script>

<!-- footer.jsp -->
