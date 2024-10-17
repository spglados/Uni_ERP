<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.uni.uni_erp.util.BERTModelService" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>BERT Model Prediction</title>
</head>
<body>
<h1>BERT Model Prediction Example</h1>
<form method="post">
  <label>Enter text with [MASK]: </label>
  <input type="text" name="inputText" value="The capital of France is [MASK]." />
  <button type="submit">Predict</button>
</form>

<%
  // 사용자가 입력한 텍스트를 가져와서 BERT 예측 실행
  String inputText = request.getParameter("inputText");
  if (inputText != null && !inputText.isEmpty()) {
    // 서버 측 BERT 모델 실행
    String prediction = BERTModelService.getBERTPrediction(inputText);

    // 예측 결과를 JSP 페이지에 출력
    out.println("<h3>Prediction: " + prediction + "</h3>");
  }
%>
</body>
</html>
