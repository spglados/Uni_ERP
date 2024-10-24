/**
 * 토스트 메시지를 생성하고 표시하는 함수
 * @param {string} message - 표시할 메시지 내용
 */
function showToast(message) {
    const toastContainer = $('#toastContainer');

    // 새로운 토스트 요소 생성
    const toastElement = $(`
        <div class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
          <div class="toast-header">
            <strong class="mr-auto">알림</strong>
            <small class="text-muted">방금</small>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="toast-body">
            ${message}
          </div>
        </div>
    `);

    // 토스트 컨테이너에 추가
    toastContainer.append(toastElement);

    // 토스트 표시
    toastElement.toast('show');

    // 토스트가 숨겨진 후 제거
    toastElement.on('hidden.bs.toast', function () {
        $(this).remove();
    });
}
