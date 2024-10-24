document.addEventListener('DOMContentLoaded', function() {
    // 모든 메뉴 토글 버튼을 선택
    const menuToggles = document.querySelectorAll('.menu-toggle');

    // 현재 페이지의 경로를 가져옵니다.
    const currentPath = window.location.pathname;

    // 서브 메뉴 링크를 선택합니다.
    const subMenuLinks = document.querySelectorAll('.sub-menu a');

    // 현재 페이지에 해당하는 서브 메뉴를 찾습니다.
    subMenuLinks.forEach(function(link) {
        if (link.getAttribute('href') === currentPath) {
            // 해당 링크의 부모 li 요소를 찾습니다.
            const parentLi = link.parentElement;

            // 부모 li의 부모 ul (즉, .sub-menu)을 찾습니다.
            const subMenu = parentLi.parentElement;

            // 서브 메뉴의 이전 형제 요소인 .menu-toggle을 찾습니다.
            const menuToggle = subMenu.previousElementSibling;

            // 해당 메뉴 토글에 'active' 클래스 추가
            menuToggle.classList.add('active');

            // 화살표 텍스트를 ▲로 변경
            const arrow = menuToggle.querySelector('.arrow');
            if (arrow) {
                arrow.textContent = '▲';
            }

            // 서브 메뉴에 'open' 클래스 추가
            subMenu.classList.add('open');

            // 다른 모든 메뉴를 비활성화
            menuToggles.forEach(function(otherToggle) {
                if (otherToggle !== menuToggle) {
                    otherToggle.classList.remove('active');
                    // 다른 화살표는 ▼로 변경
                    const otherArrow = otherToggle.querySelector('.arrow');
                    if (otherArrow) {
                        otherArrow.textContent = '▼';
                    }
                }
            });

            // 모든 서브 메뉴을 닫고 현재 서브 메뉴만 열어둡니다.
            const allSubMenus = document.querySelectorAll('.sub-menu');
            allSubMenus.forEach(function(otherSubMenu) {
                if (otherSubMenu !== subMenu) {
                    otherSubMenu.classList.remove('open');
                }
            });
        }
    });

    // 메뉴 토글 클릭 이벤트 핸들러
    menuToggles.forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            // 현재 클릭된 메뉴의 active 클래스 토글
            const isActive = this.classList.contains('active');

            // 모든 메뉴 토글에서 active 클래스 제거
            menuToggles.forEach(function(item) {
                item.classList.remove('active');
                // 모든 화살표를 ▼로 변경
                const arrow = item.querySelector('.arrow');
                if (arrow) {
                    arrow.textContent = '▼';
                }
            });

            // 모든 서브 메뉴 닫기
            const allSubMenus = document.querySelectorAll('.sub-menu');
            allSubMenus.forEach(function(subMenu) {
                subMenu.classList.remove('open');
            });

            if (!isActive) {
                // 현재 클릭된 메뉴 토글에 active 클래스 추가
                this.classList.add('active');
                // 해당 서브 메뉴 열기
                const subMenu = this.nextElementSibling;
                subMenu.classList.add('open');

                // 화살표 텍스트 변경
                const arrow = this.querySelector('.arrow');
                if (arrow) {
                    arrow.textContent = '▲';
                }
            }
        });
    });
});