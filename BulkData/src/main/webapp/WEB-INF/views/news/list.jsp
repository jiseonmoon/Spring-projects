<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
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
			</tr>
		</thead>
		<c:forEach items="${newsList }" var="news">
			<tr>
				<td><c:out value="${news.nno }" /></td>
				<td><c:out value="${news.ntitle }" /></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
						value="${news.ndate }" /></td>
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
	<form id='actionForm' action="/news/list" method='get'>
		<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum }'>
		<input type='hidden' name='amount' value='${pageMaker.cri.amount }'>
	</form>
	<script type="text/javascript">
		$(document).ready(
				function() {
					var actionForm = $("#actionForm");
					$("ul li a").on(
							"click",
							function(e) {
								e.preventDefault();
								console.log('click');
								actionForm.find("input[name='pageNum']").val(
										$(this).attr("href"));
								actionForm.submit();
							});
				});
	</script>
</body>
</html>