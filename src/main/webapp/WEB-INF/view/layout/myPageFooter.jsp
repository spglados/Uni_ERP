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
</body>
</div>
<footer class="footer">
    <div class="inner--footer">
        <p class="f--logo">Uni ERP System</p>
        <ul class="f--info">
            <li class="f--contact"><p><i class="fa-solid fa-phone"></i>+82 000-0000</p></li>
            <li class="f--contact"><p><i class="fa-solid fa-envelope"></i>unierp@gmail.com</p></li>
        </ul>
        <p class="copyright">Copyright © Uni Co. All Right Reserved.</p>
</footer>
</html>
