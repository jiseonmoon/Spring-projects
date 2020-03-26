<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>get page</h1>
	<div>
		<label>bno</label><input name='bno'
			value='<c:out value="${board.bno }"/>' readonly="readonly">
	</div>
	<div>
		<label>title</label><input name='title'
			value='<c:out value="${board.title }"/>' readonly="readonly">
	</div>
	<div>
		<label>text area</label>
		<textarea rows="3" name='content' readonly="readonly"><c:out
				value="${board.content }" /></textarea>
	</div>
	<div>
		<label>writer</label><input name='writer'
			value='<c:out value="${board.writer }"/>' readonly="readonly">
	</div>
	<div>
		<button data-oper='modify'>modify</button>
		<button data-oper='list'>list</button>
	</div>
	<form id='operForm' action="/board/modify" method="get">
		<input type="hidden" id='bno' name='bno'
			value='<c:out value="${board.bno }"/>'> <input type="hidden"
			name='pageNum' value='<c:out value="${cri.pageNum }"/>'> <input
			type="hidden" name='amount' value='<c:out value="${cri.amount }"/>'>
		<input type="hidden" name='keyword'
			value='<c:out value="${cri.keyword }"/>'> <input
			type="hidden" name='type' value='<c:out value="${cri.type }"/>'>
	</form>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var operForm = $("#operForm");
			$("button[data-oper='modify']").on("click", function(e) {
				operForm.attr("action", "/board/modify").submit();
			});
			$("button[data-oper='list']").on("click", function(e) {
				operForm.find("#bno").remove();
				operForm.attr("action", "/board/list")
				operForm.submit();
			});
		});
	</script>
</body>
</html>