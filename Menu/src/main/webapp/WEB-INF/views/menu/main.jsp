<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Menu</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<c:forEach var="level1" items="${levelMap[(1).intValue()] }">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-expanded="false">${nodeMap[level1].data }
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu" role="menu">
								<c:forEach var="level2" items="${levelMap[(2).intValue()] }">
									<c:if test="${noMap[level2] eq level1 }">
										<li><a href="#">${nodeMap[level2].data }</a></li>
									</c:if>
								</c:forEach>
							</ul></li>
					</c:forEach>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<ul>
		<c:forEach var="level1" items="${levelMap[(1).intValue()] }">
			<li>${nodeMap[level1].data }<c:forEach var="level2"
					items="${levelMap[(2).intValue()] }">
					<c:if test="${noMap[level2] eq level1 }">
						<ul>
							<li>${nodeMap[level2].data }</li>
						</ul>
					</c:if>
				</c:forEach>
			</li>
		</c:forEach>
	</ul>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>