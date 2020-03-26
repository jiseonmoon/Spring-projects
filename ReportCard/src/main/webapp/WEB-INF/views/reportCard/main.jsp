<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table thead tr th {
	width: 100px;
}

table tbody tr td {
	height: 50px;
	border: 1px solid black;
}

.aboveAverage {
	background-color: green;
}

.belowAverage {
	background-color: yellow;
}
</style>
</head>
<body>
	<form>
		<select onchange="this.form.submit()" name="criterion">
			<c:forEach var="subject" items="${subjectList }">
				<c:if test="${subject ne 'avg' }">
					<option value="${subject }"
						<c:if test="${criterion eq subject }">selected="selected"</c:if>>${subject }</option>
				</c:if>
			</c:forEach>
		</select>
	</form>
	<table>
		<thead>
			<tr>
				<th>이름</th>
				<c:forEach var="subject" items="${subjectList }">
					<th>${subject }</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${entryList }">
				<tr>
					<td>${entry.key }</td>
					<c:forEach var="subject" items="${subjectList }">
						<c:set var="rank" value="Rank" />
						<c:set var="subjectRank" value="${subject }${rank }" />
						<td
							class="<c:if test='${entry.value[subject]>subjectMap["avg"][subject]}'>aboveAverage</c:if><c:if test='${entry.value[subject]<subjectMap["avg"][subject]}'>belowAverage</c:if>">${entry.value[subject]}${entry.value[subjectRank] }</td>
					</c:forEach>
				</tr>
			</c:forEach>
			<tr>
				<td>과목별 총점</td>
				<c:forEach var="subject" items="${subjectList }">
					<td>${subjectMap["sum"][subject] }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>과목별 평균</td>
				<c:forEach var="subject" items="${subjectList }">
					<td>${subjectMap["avg"][subject] }</td>
				</c:forEach>
			</tr>
		</tbody>
	</table>
</body>
</html>