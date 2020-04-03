<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.15/c3.min.js" />
<style>
#container {
	display: flex;
	justify-content: space-around;
}

#statisticsContainer {
	width: 30%;
	display: flex;
	flex-direction: column;
}

#chartContainer {
	width: 50%;
	display: flex;
	flex-direction: column;
}
</style>
</head>
<body>
	<h1>statistics page</h1>
	<div id="container">
		<div id="statisticsContainer">
			<form>
				<select onchange="this.form.submit()" name="statisticsStandard">
					<option value="month"
						<c:if test="${statisticsStandard eq 'month' }">selected="selected"</c:if>>월</option>
					<option value="day"
						<c:if test="${statisticsStandard eq 'day' }">selected="selected"</c:if>>일</option>
					<option value="hour"
						<c:if test="${statisticsStandard eq 'hour' }">selected="selected"</c:if>>시</option>
				</select>
			</form>
			<table>
				<thead>
					<tr>
						<th><c:choose>
								<c:when test="${statisticsStandard eq 'month' }">월</c:when>
								<c:when test="${statisticsStandard eq 'day' }">일</c:when>
								<c:when test="${statisticsStandard eq 'hour' }">시</c:when>
							</c:choose></th>
						<th>개수</th>
					</tr>
				</thead>
				<c:forEach items="${statisticsList }" var="statistics">
					<tr>
						<td><c:out value="${statistics.date }" /></td>
						<td><c:out value="${statistics.count }" /></td>
					</tr>
				</c:forEach>
				<tr>
				</tr>
			</table>
			<form name="excelForm" id="excelForm" method="POST"
				action="/news/excelDown">
				<input type="hidden" name="statisticsStandard"
					value="${statisticsStandard }"><input type="submit"
					id="excelDown" value="엑셀 다운" />
			</form>
		</div>
		<div id="chartContainer">
			<select id="chartType">
				<option value="line">라인</option>
				<option value="pie">파이</option>
				<option value="bar">바</option>
			</select>
			<div id="lineChart"></div>
			<div id="pieChart"></div>
			<div id="barChart"></div>
		</div>
	</div>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/d3/5.15.0/d3.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.15/c3.min.js"></script>
	<script type="text/javascript">
		$(document).ready(
				function() {
					//차트
					var dateList = [ 'x' ];
					var countList = [ '뉴스 개수' ];
					var statisticsList = [];
					<c:forEach items="${statisticsList}" var="statistics">
					dateList.push("${statistics.date}");
					countList.push("${statistics.count}");
					statisticsList.push([ "${statistics.date}",
							"${statistics.count}" ]);
					</c:forEach>
					// 라인 차트
					var lineChart = c3.generate({
						bindto : '#lineChart',
						data : {
							x : 'x',
							columns : [ dateList, countList ]
						}
					});
					// 파이 차트
					var pieChart = c3.generate({
						bindto : '#pieChart',
						data : {
							columns : statisticsList,
							type : 'pie',
						}
					});
					// 바 차트
					var barChart = c3.generate({
						bindto : '#barChart',
						data : {
							x : 'x',
							columns : [ dateList, countList ],
							type : 'bar'
						},
						bar : {
							width : {
								ratio : 0.5
							}
						}
					});
					$('#lineChart').show();
					$('#pieChart').hide();
					$('#barChart').hide();
					$('#chartType').on('change', function() {
						$('#lineChart').hide();
						$('#pieChart').hide();
						$('#barChart').hide();
						switch ($(this).val()) {
						case 'line':
							$('#lineChart').show();
							break;
						case 'pie':
							$('#pieChart').show();
							break;
						case 'bar':
							$('#barChart').show();
							break;
						}
					});
				});
	</script>
</body>
</html>