document.addEventListener('DOMContentLoaded', function() {
            // 모든 <tr> 요소에 클릭 이벤트 추가
            document.querySelectorAll('tbody tr').forEach(function(row) {
                row.addEventListener('click', function() {
                    // 데이터 속성에서 URL 가져오기
                    var url = this.getAttribute('data-url');
                    // URL로 리디렉션
                    window.location.href = url;
                });
            });
        });