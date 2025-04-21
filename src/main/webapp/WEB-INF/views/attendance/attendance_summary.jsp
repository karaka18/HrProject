<!-- attendance/summary.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>
<html>
<head>
<title>근무 기록 요약</title>
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
				<li class="active"><a href="/attendance/records">근무 기록</a></li>
				<li><a href="/attendance/leave">휴가 내역</a></li>
				<li><a href="/attendance/status">근태 상태</a></li>
			</ul>
		</div>

		<div class="main-content">
			<div class="page-title">근무 기록 요약</div>

			<table>
				<thead>
					<tr>
						<th>날짜</th>
						<th>출근 시간</th>
						<th>퇴근 시간</th>
						<th>지각 여부</th>
						<th>조퇴 여부</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="record" items="${attendanceList}">
						<tr>
							<td>${record.workDate}</td>
							<td><c:choose>
									<c:when test="${not empty record.checkInTime}">
                                    ${record.checkInTime}
                                </c:when>
									<c:otherwise>--:--</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${not empty record.checkOutTime}">
                                    ${record.checkOutTime}
                                </c:when>
									<c:otherwise>--:--</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${record.isLate eq 'Y'}">⭕ 지각</c:when>
									<c:otherwise>✅ 정상</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${record.isEarlyLeave eq 'Y'}">❗ 조퇴</c:when>
									<c:otherwise>✅ 정상</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
<jsp:include page="../common/footer2.jsp" />