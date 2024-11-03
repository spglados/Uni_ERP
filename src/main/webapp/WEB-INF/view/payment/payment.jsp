<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<!-- Select2 CSS 추가 -->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>

<link rel="stylesheet" href="/css/payment/payment.css">
<link rel="stylesheet" href="/css/payment/subscribe.css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Select2 JS 추가 -->
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script src="https://js.tosspayments.com/v1"></script>

<main class="main-container">

    <section class="premium-section">
        <div class="intro-section">
            <i class="fas fa-star intro-icon"></i>
            <h2 class="title--heading">프리미엄 서비스 시작하기</h2>
            <p>
                첫 가게 등록 시 <strong>50,000원</strong>, <br><br>이후 추가하는 가게는 매달 <strong>30,000원</strong>의 비용으로 프리미엄 서비스를
                무제한으로 이용하세요.<br><br>
                고급 기능과 편리한 관리 도구로 비즈니스를 더욱 성장시킬 수 있습니다.
            </p>
        </div>


        <div class="item--wrap">
            <c:choose>
            <c:when test="${membership == 'PREMIUM'}">
            <div class="plan">
                <div class="inner">
                    <span class="pricing">30,000원<small> / 추가 가게</small></span>
                    <p class="title">추가 등록 패키지</p>
                    <p class="info">추가 등록을 원하시는 사장님들을 위한<br>추가 패키지</p>
                    <ul class="features">
                        <li>
                            <span class="icon">
                                <i class="fas fa-store"></i>
                            </span>
                            <span><strong>추가 가게</strong> 등록</span>
                        </li>
                        <li>
                            <span class="icon">
                                <i class="fas fa-cogs"></i>
                            </span>
                            <span><strong>여러 개의 가게</strong>를 등록해보세요 !</span>
                        </li>
                    </ul>
                    <div class="action">
                        <div class="form-group">
                            <label for="desiredDay" class="payment-date-label">
                                <i class="fas fa-calendar-alt"></i> 결제일 변경 시 선택
                            </label>
                            </c:when>
                            <c:otherwise>
                            <!-- 입문 패키지 -->
                            <div class="plan">
                                <div class="inner">
                                    <span class="pricing">50,000원<small> / 첫 결제</small></span>
                                    <p class="title">입문 패키지</p>
                                    <p class="info">처음 등록하는 사장님들을 위한<br>입문 패키지</p>
                                    <ul class="features">
                                        <li>
                            <span class="icon">
                                <i class="fas fa-store"></i>
                            </span>
                                            <span><strong>첫번째 가게</strong> 등록</span>
                                        </li>
                                        <li>
                            <span class="icon">
                                <i class="fas fa-cogs"></i>
                            </span>
                                            <span><strong>가게 관리</strong>의 첫걸음!</span>
                                        </li>
                                    </ul>
                                    <div class="action">
                                        <div class="form-group">
                                            <label for="desiredDay" class="payment-date-label">
                                                <i class="fas fa-calendar-alt"></i> 매달 결제일 설정
                                            </label>
                                            </c:otherwise>
                                            </c:choose>
                                            <select id="desiredDay" name="desiredDay" required aria-label="원하는 결제일"
                                                    class="select2 payment-date-select">
                                                <!-- 1일부터 31일까지 옵션 생성 -->
                                                <% for (int day = 1; day <= 31; day++) { %>
                                                <option value="<%= day %>"><%= day %>일</option>
                                                <% } %>
                                            </select>
                                        </div>
                                        <button id="card_button" class="button" onclick="Pay()">
                                            <span id="button-text">지금 등록하기</span>
                                            <span id="button-spinner" style="display: none;">
                                <i class="fas fa-spinner fa-spin"></i>
                            </span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="warning">
                            <b><i class="fas fa-exclamation-triangle"></i> 주의사항 <i
                                    class="fas fa-exclamation-triangle"></i></b><br><br>
                            모든 결제를 취소하면 다음 결제 시 첫 결제는 <strong>50,000원</strong>으로 시작하며, 이후 매달 <strong>30,000원</strong>이
                            청구됩니다.
                        </div>
    </section>

</main>

<script>
    $(document).ready(function () {
        $('.select2').select2({
            minimumResultsForSearch: Infinity, // 검색창 숨기기
            width: '100%',
            placeholder: "결제일을 선택하세요",
            templateResult: formatState,
            templateSelection: formatState
        });

        // 오늘 날짜로 결제일 자동 설정
        const today = new Date();
        const todayDay = today.getDate();
        $('#desiredDay').val(todayDay).trigger('change');
    });

    // 선택된 결제일 스타일링 (옵션)
    function formatState(state) {
        if (!state.id) {
            return state.text;
        }
        var $state = $('<span><i class="fas fa-calendar-alt" style="margin-right:8px;color:#1E90FF;"></i>' + state.text + '</span>');
        return $state;
    };

    // 정기결제
    function Pay() {
        const payButton = document.getElementById('card_button');
        const buttonText = document.getElementById('button-text');
        const buttonSpinner = document.getElementById('button-spinner');

        // 버튼 비활성화 및 스피너 표시
        payButton.disabled = true;
        buttonText.style.display = 'none';
        buttonSpinner.style.display = 'inline-block';

        const clientKey = "test_ck_E92LAa5PVbPWkE0RkGbW87YmpXyJ"; // 서버에서 전달받은 클라이언트 키
        const tossPayments = TossPayments(clientKey);
        const customerKey = Math.random().toString(36).substring(2, 12); // 고객 고유키를 서버로부터 받아옵니다.

        const desiredDay = document.getElementById("desiredDay").value;

        tossPayments.requestBillingAuth("카드", {
            customerKey: customerKey, // 서버에서 전달받은 고객 키
            successUrl: "http://localhost:8080/payment/success?desiredPayDate=" + desiredDay, // 성공 시 리디렉션 URL
            failUrl: "http://localhost:8080/payment/fail" // 실패 시 리디렉션 URL
        })
            .catch(function (error) {
                // 버튼 활성화 및 스피너 숨기기
                payButton.disabled = false;
                buttonText.style.display = 'inline-block';
                buttonSpinner.style.display = 'none';

                if (error.code === "USER_CANCEL") {
                    alert("결제를 취소했습니다.");
                } else if (error.code === "INVALID_CARD_COMPANY") {
                    alert(error.message);
                } else {
                    alert("결제 중 오류가 발생했습니다. 다시 시도해주세요.");
                }
            });
    }
</script>

<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
</html>
