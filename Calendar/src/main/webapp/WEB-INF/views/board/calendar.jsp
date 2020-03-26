<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table thead tr th {
	width: 125px;
}

table tbody tr td {
	height: 125px;
	border: 1px solid black;
}

#btn-open-dialog {
	height: 100%;
}

.yes {
	color: red;
}

.scheduleName {
	color: blue;
}

#dialog-background {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, .3);
	z-index: 10;
}

#my-dialog {
	display: none;
	position: fixed;
	left: calc(50% - 250px);
	top: calc(50% - 250px);
	width: 500px;
	height: 500px;
	background: #fff;
	z-index: 11;
	padding: 10px;
}

#btn-close-dialog {
	float: right;
}
</style>
</head>
<body>
	<a
		href="calendar?uno=${uno }&year=${calendarDTO.year }&month=${calendarDTO.month-1 }">&lt;</a>
	<fmt:formatDate pattern="yyyy-MM" value="${calendarDTO.date }" />
	<fmt:setLocale value="en" scope="session" />
	<fmt:formatDate pattern="MMMM" value="${calendarDTO.date }" />
	<a
		href="calendar?uno=${uno }&year=${calendarDTO.year }&month=${calendarDTO.month+1 }">&gt;</a>
	<table>
		<thead>
			<tr>
				<th>일</th>
				<th>월</th>
				<th>화</th>
				<th>수</th>
				<th>목</th>
				<th>금</th>
				<th>토</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="week" items="${calendarDTO.dateList }" step="7"
				varStatus="status">
				<tr>
					<c:forEach var="date" items="${calendarDTO.dateList }"
						begin="${status.index }" end="${status.index+6 }">
						<td><div
								id="<c:if test="${date ne null }">btn-open-dialog</c:if>"
								data-date="${date.date }">
								<c:out value="${date.day }" />
								<br>
								<c:if test="${holidayMap.containsKey(date.date) }">
									<c:forEach var="holiday" items="${holidayMap[date.date] }">
										<div
											class="<c:if test='${holiday.yesOrNo eq "Y" }'>yes</c:if>">
											${holiday.holidayName }<br>
										</div>
									</c:forEach>
								</c:if>
								<c:if test="${diaryMap.containsKey(date.date) }">
									<c:forEach var="diary" items="${diaryMap[date.date] }">
										<div class="scheduleName">
											${diary.scheduleName }<br>
										</div>
									</c:forEach>
								</c:if>
							</div></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="my-dialog">
		<div id="btn-close-dialog">X</div>
		<div>
			<h3>일자</h3>
			<input type="text" id="scheduleDate" name="scheduleDate"
				readonly="readonly">
			<h3>일정</h3>
			<ul class="scheduleUL">
			</ul>
		</div>
		<div>
			<input type="text" id="scheduleName" name="scheduleName">
			<button id="registerBtn" type="button">추가</button>
		</div>
	</div>
	<div id="dialog-background"></div>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="/resources/js/schedule.js"></script>
	<script>
		$(function() {
			$("#btn-open-dialog,#dialog-background,#btn-close-dialog")
					.click(
							function() {
								$("#scheduleDate").val($(this).data("date"));
								$("#scheduleName").val("");
								$("#my-dialog,#dialog-background").toggle();

								var unoValue = "<c:out value='${uno}'/>";
								var scheduleDate = $("#scheduleDate").val();
								var scheduleUL = $(".scheduleUL");

								function showList() {
									scheduleService
											.getList(
													{
														uno : unoValue,
														scheduleDate : scheduleDate
													},
													function(list) {
														var str = "";
														if (list == null
																|| list.length == 0) {
															scheduleUL.html("");
															return;
														}
														for (var i = 0, len = list.length || 0; i < len; i++) {
															str += "<li data-dno='"+list[i].dno+"'>"
																	+ list[i].scheduleName
																	+ "<button id='modBtn' type='button'>수정</button><button id='removeBtn' type='button'>삭제</button>"
																	+ "</li>";
														}
														scheduleUL.html(str);
													});
								}

								$("#registerBtn").on(
										"click",
										function() {
											var scheduleName = $(
													"#scheduleName").val();
											if (scheduleName == "") {
												return;
											}
											scheduleService.add({
												uno : unoValue,
												scheduleDate : scheduleDate,
												scheduleName : scheduleName
											}, function(result) {
												alert(result);
												showList();
												$("#scheduleName").val("");
												location.reload();
											});

										});
								showList();
							});
		});
	</script>
</body>
</html>