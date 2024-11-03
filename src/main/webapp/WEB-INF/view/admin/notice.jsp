<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
       <form id="myForm">
          <div class="form-group">
             <label for="title">제목:</label>
             <input type="text" class="form-control" id="title" name="title" maxlength="100">
          </div>
          <div class="form-group">
             <label for="category">카테고리:</label>
             <select class="form-control" id="category" name="category">
                <option value="업데이트">업데이트</option>
                <option value="안내">안내</option>
             </select>
          </div>
          <div class="form-group">
             <label for="content">내용:</label>
             <textarea class="form-control" id="content" name="content"></textarea>
          </div>
          <button type="submit" class="btn btn-primary">Submit</button>
       </form>
    </div>
    <script>
      const form = document.getElementById('myForm');
      form.addEventListener('submit', (e) => {
        e.preventDefault(); // Prevent form submission
        const title = document.getElementById('title').value.trim();
        const category = document.getElementById('category').value.trim();
        const content = document.getElementById('content').value.trim();

        if (title === '') {
          alert('제목을 입력해주세요');
          return;
        }

        if (content === '') {
          alert('내용을 입력해주세요');
          return;
        }

        const data = new FormData();
        data.append('title', title);
        data.append('category', category);
        data.append('content', content);
        fetch('/admin/notice', {
          method: 'POST',
          body: data,
        })
        .then((response) => {
          if (response.ok) {
            return response.text();
          } else {
            throw new Error('오류 발생');
          }
        })
        .then((data) => {
          alert(data);
          window.location.href = '/admin/main';
        })
        .catch((error) => console.error(error));
      });
    </script>
</body>
</html>