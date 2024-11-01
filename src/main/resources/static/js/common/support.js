document.getElementById('submit-btn').addEventListener('click', (e) => {
  e.preventDefault(); // Prevent form submission

  const title = document.getElementById('title').value.trim();
  const content = document.getElementById('content').value.trim();

  if (title === '' || content === '') {
    alert('모든 칸을 채워주세요.');
    return;
  }

  const formData = new FormData();
  formData.append('title', title);
  formData.append('content', content);

  fetch('/support/contact', {
    method: 'POST',
    body: formData,
  })
  .then((response) => {
    if (!response.ok) {
      alert('에러가 발생했습니다.');
      return;
    }
    return response.text();
  })
  .then((result) => {
    alert(result);
    location.reload();
  })
  .catch((error) => {
    alert('에러가 발생했습니다.');
  });
});