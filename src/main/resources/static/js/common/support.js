document.getElementById('submit-btn').addEventListener('click', (e) => {

  const name = document.getElementById('name').value.trim();
  const email = document.getElementById('email').value.trim();
  const tel = document.getElementById('tel').value.trim();
  const content = document.getElementById('content').value.trim();

  if (name === '' || email === '' || tel === '' || content === '') {
    alert('모든 칸을 채워주세요.');
    return;
  }

  if (!validateTel(tel)) {
    alert('유효한 전화번호가 아닙니다.');
    return;
  }

  if (!validateEmail(email)) {
    alert('유효한 이메일이 아닙니다.');
    return;
  }

  const formData = new FormData();
  formData.append('name', name);
  formData.append('email', email);
  formData.append('tel', tel);
  formData.append('content', content);

  fetch('/support/contact', {
    method: 'POST',
    body: formData,
  })
  .then((response) => {
    if (!response.ok) {
      alert('err');
      return;
    }
    return response.text();
  })
  .then((result) => {
    alert(result);
    location.reload();
  })
  .catch((error) => {
    alert('err');
  });
});

function validateEmail(email) {
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailRegex.test(email);
}

function validateTel(tel) {
  const telRegex = /^\d{9,11}$/;
  return telRegex.test(tel);
}