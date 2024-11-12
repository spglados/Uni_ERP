    <%--
      Created by IntelliJ IDEA.
      User: namch
      Date: 24. 10. 10.
      Time: 오전 10:20
      To change this template use File | Settings | File Templates.
    --%>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <script>
        const wrapper = document.querySelector('.site__header');
        const toggleButton = document.querySelector('.toggle');
        const menu = document.querySelector('.menu');
        const menuItem = Array.from(menu.querySelectorAll('li a'));

        toggleButton.addEventListener('click', (e) => {
        const target = e.currentTarget;
        if (!target.classList.contains('active')) {
        target.classList.add('active');
        wrapper.classList.add('darkmode');
        } else {
        target.classList.remove('active');
        wrapper.classList.remove('darkmode');
        }
        });

        menu.addEventListener('click', (e) => {
        let target = e.target;
        menuItem.forEach((item) => item.classList.remove('active'));
        target.classList.add('active');
        });

        </script>

        <footer class="footer">
        <div class="inner--footer">
        <p class="f--logo">Uni ERP System</p>
        <ul class="f--info">
        <li class="f--contact">
        <p><i class="fa-solid fa-phone"></i> +82 051-123-4567</p>
        </li>
        <li class="f--contact">
        <p><i class="fa-solid fa-envelope"></i> unierp@gmail.com</p>
        </li>
        <li class="f--contact">
        <p><i class="fa-solid fa-map-marker-alt"></i> 부산광역시 부산진구</p>
        </li>
        </ul>
        <ul class="f--links">
        <li><a href="#">홈</a></li>
        <li><a href="#">서비스</a></li>
        <li><a href="#">문의</a></li>
        <li><a href="#">블로그</a></li>
        <li><a href="#">이용약관</a></li>
        <li><a href="#">개인정보처리방침</a></li>
        </ul>
        <div class="f--social">
        <a href="#" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
        <a href="#" aria-label="Twitter"><i class="fab fa-twitter"></i></a>
        <a href="#" aria-label="LinkedIn"><i class="fab fa-linkedin-in"></i></a>
        <a href="#" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
        </div>
        </div>
        <div class="footer__bottom">
        <p class="copyright">&copy; 2024 Uni Co. All Rights Reserved.</p>
        <p class="developer">
        개발자:
        <a href="https://github.com/spglados" class="dev-name" target="_blank">이건우</a>,
        <a href="https://github.com/kyeonghooon" class="dev-name" target="_blank">강경훈</a>,
        <a href="https://github.com/Nam-Cheol" class="dev-name" target="_blank">김남철</a>,
        <a href="#" class="dev-name">방민석</a>,
        <a href="#" class="dev-name">서치원</a>,
        <a href="https://github.com/CHYIJE" class="dev-name" target="_blank">최이제</a>
        </p>
        </div>
        </footer>


        </body>
        </html>
