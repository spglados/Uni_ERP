<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Details</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <div class="card">
        <div class="card-header">
            <h2>${contact.title}</h2>
        </div>
        <div class="card-body">
            <p>${contact.content}</p>
        </div>
    </div>

    <div class="answer-section mt-4">
        <h3>대답:</h3>
        <form id="answerForm">
            <div class="form-group">
                <textarea id="answer" class="form-control" rows="5"></textarea>
            </div>
            <input type="hidden" id="contactId" value="${contact.id}"/>
            <button type="submit" class="btn btn-primary">확인</button>
        </form>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    document.getElementById("answerForm").addEventListener("submit", function(event) {

        const answer = document.getElementById("answer").value.trim();
        const contactId = document.getElementById("contactId").value;

        if (!answer) {
            alert("대답.");
            return;
        }

        const formData = new FormData();
        formData.append('contactId', contactId);
        formData.append('answer', answer);

        fetch('/admin/response', {
            method: 'POST',
            body: formData,
        })
        .then(response => response.text())
        .then(message => {
            alert(message);
            window.location.href = '/admin/contactList';
        })
        .catch(error => {
            alert("오류");
        });
    });
</script>
</body>
</html>
