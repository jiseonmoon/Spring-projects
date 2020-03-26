<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>register page</h1>
	<form role="form" action="/board/register" method="post">
		<div>
			<label>title</label><input name='title'>
		</div>
		<div>
			<label>text area</label>
			<textarea rows="3" name='content'></textarea>
		</div>
		<div>
			<label>writer</label> <input name='writer'>
		</div>
		<div>
			<button type="submit">submit button</button>
			<button type="reset">reset button</button>
		</div>
	</form>
</body>
</html>