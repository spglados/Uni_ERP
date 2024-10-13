// 하위 메뉴 토글
document.addEventListener("DOMContentLoaded", function() {
    // 인사 관리 하위 메뉴 토글
    document.querySelectorAll('.hr-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('sub-menu')) {
                subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });

    // 재고 관리 하위 메뉴 토글
    document.querySelectorAll('.inventory-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('sub-menu')) {
                subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });

    // 매출 관리 하위 메뉴 토글
    document.querySelectorAll('.sales-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('sub-menu')) {
                subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });

    // 상품 관리 하위 메뉴 토글
    document.querySelectorAll('.product-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('sub-menu')) {
                subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });
});