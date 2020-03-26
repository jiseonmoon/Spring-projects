<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>list page</h1>
	<table>
		<thead>
			<tr>
				<th>#번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>수정일</th>
			</tr>
		</thead>
		<c:forEach items="${list }" var="board">
			<tr>
				<td><c:out value="${board.bno }" /></td>
				<td><a class='move' href='<c:out value="${board.bno }"/>'><c:out
							value="${board.title }" /></a></td>
				<td><c:out value="${board.writer }" /></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd"
						value="${board.regdate }" /></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd"
						value="${board.updateDate }" /></td>
			</tr>
		</c:forEach>
	</table>
	<ul>
		<c:if test="${pageMaker.prev }">
			<li><a href="${pageMaker.startPage-1 }">previous</a></li>
		</c:if>
		<c:forEach var="num" begin="${pageMaker.startPage }"
			end="${pageMaker.endPage }">
			<li><a href="${num }">${num }</a></li>
		</c:forEach>
		<c:if test="${pageMaker.next }">
			<li><a href="${pageMaker.endPage+1 }">next</a></li>
		</c:if>
	</ul>
	<form id='actionForm' action="/board/list" method='get'>
		<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum }'>
		<input type='hidden' name='amount' value='${pageMaker.cri.amount }'>
		<input type='hidden' name='type'
			value='<c:out value="${pageMaker.cri.type }"/>'> <input
			type='hidden' name='keyword'
			value='<c:out value="${pageMaker.cri.keyword }"/>'>
	</form>
	<button id='regBtn' type="button">register new board</button>
	<form id='searchForm' action="/board/list" method='get'>
		<select name='type'>
			<option value=""
				<c:out value="${pageMaker.cri.type == null?'selected':'' }"/>>--</option>
			<option value="T"
				<c:out value="${pageMaker.cri.type eq 'T'?'selected':'' }"/>>제목</option>
			<option value="C"
				<c:out value="${pageMaker.cri.type eq 'C'?'selected':'' }"/>>내용</option>
			<option value="W"
				<c:out value="${pageMaker.cri.type eq 'W'?'selected':'' }"/>>작성자</option>
			<option value="TC"
				<c:out value="${pageMaker.cri.type eq 'TC'?'selected':'' }"/>>제목
				or 내용</option>
			<option value="TW"
				<c:out value="${pageMaker.cri.type eq 'TW'?'selected':'' }"/>>제목
				or 작성자</option>
			<option value="TCW"
				<c:out value="${pageMaker.cri.type eq 'TWC'?'selected':'' }"/>>제목
				or 내용 or 작성자</option>
		</select> <input type='text' name='keyword' /> <input type='hidden'
			name='pageNum' value='<c:out value="${pageMaker.cri.pageNum }"/>' />
		<input type='hidden' name='amount'
			value='<c:out value="${pageMaker.cri.amount }"/>' />
		<button>search</button>
	</form>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var result = '<c:out value="${result}"/>';
							$("#regBtn").on("click", function() {
								self.location = "/board/register";
							});
							var actionForm = $("#actionForm");
							$("ul li a").on(
									"click",
									function(e) {
										e.preventDefault();
										console.log('click');
										actionForm
												.find("input[name='pageNum']")
												.val($(this).attr("href"));
										actionForm.submit();
									});
							$(".move")
									.on(
											"click",
											function(e) {
												e.preventDefault();
												actionForm
														.append("<input type='hidden' name='bno' value='"
																+ $(this).attr(
																		"href")
																+ "'>");
												actionForm.attr("action",
														"/board/get");
												actionForm.submit();
											});
							var searchForm = $("#searchForm");
							$("#searchForm button")
									.on(
											"click",
											function(e) {
												if (!searchForm.find(
														"option:selected")
														.val()) {
													alert("검색종류를 선택하세요");
													return false;
												}
												if (!searchForm
														.find(
																"input[name='keyword']")
														.val()) {
													alert("키워드를 입력하세요");
													return false;
												}
												searchForm
														.find(
																"input[name='pageNum']")
														.val("1");
												e.preventDefault();
												searchForm.submit();
											});
						});
	</script>
</body>
</html>