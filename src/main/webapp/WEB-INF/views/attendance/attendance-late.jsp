<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>지각 내역</title>
<style>
.container {
	display: flex;
}

.sidebar {
	width: 220px;
	background-color: #f0f0f0;
	padding: 20px;
	height: 100vh;
}

.main-content {
	flex-grow: 1;
	padding: 30px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

table, th, td {
	border: 1px solid #999;
}

th, td {
	padding: 10px;
	text-align: center;
}

.page-title {
	font-size: 28px;
	font-weight: bold;
	text-align: center;
	margin-bottom: 20px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="sidebar">
			<h2>근태 메뉴</h2>
			<ul>
				<li><a href="/attendance/main">출퇴근 현황</a></li>
				<li><a href="/attendance/records">근무 기록</a></li>
				<li class="active"><a href="/attendance/attendance-late">지각
						내역</a></li>
				<li><a href="/attendance/leave">휴가 내역</a></li>
			</ul>
		</div>

		<div class="main-content">
			<div class="page-title">지각 내역</div>

			<form method="get" action="/attendance/attendance-late">
				<label>시작일: <input type="date" name="startDate" required></label>
				<label>종료일: <input type="date" name="endDate" required></label>
				<button type="submit">조회</button>
			</form>

			<c:if test="${not empty attendanceLateList}">
				<table>
					<thead>
						<tr>
							<th>날짜</th>
							<th>출근 시간</th>
							<th>지각 사유</th>
							<th>비고</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="late" items="${attendanceLateList}">
							<tr>
								<td>${late.date}</td>
								<td>${late.arrivalTime}</td>
								<td><c:choose>
										<c:when test="${not empty late.reason}">${late.reason}</c:when>
										<c:otherwise>미입력</c:otherwise>
									</c:choose></td>
								<td>지각 ${late.lateMinutes}분</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

			<c:if test="${empty attendanceLateList}">
				<p>지각 내역이 없습니다.</p>
			</c:if>
		</div>
	</div>
</body>
</html>
