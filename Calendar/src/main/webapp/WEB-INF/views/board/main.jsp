<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>나의 다이어리</h1>
	<form action="/board/signIn" method="post">
		<label for="id">아이디</label><br> <input type="text" id="id"
			name="id"><br> <label for="password">비밀번호</label><br>
		<input type="password" id="password" name="password"><br>
		<input type="submit" value="로그인"><br> <a
			href="/board/signUp">회원가입</a>
	</form>
</body>
</html>