<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>

<head>
 <meta charset="UTF-8">
<title>지각 현황</title>
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
			<h2>지각 현황</h2>
			<ul>
				<li><a href="/attendance/main">출퇴근 기록부 및 현황</a></li>
				<li><a href="/attendance/records">근무 조회</a></li>
				<li class="active"><a href="/attendance/attendance-late">지각 현황</a></li>
				<li><a href="/attendance/leave">휴가 내역 확인</a></li>
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
							<th>근무 일자</th>
							<th>출근 시간</th>
							<th>지각 날짜</th>
							<th>지각 시간</th>
							<th>지각 사유</th>
							<th>상태</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="late" items="${attendanceLateList}">
							<tr>
								<td>${late.workDate}</td>
								<td>${late.checkInTime}</td>
								<td>${late.lateDate}</td>
								<td>${late.lateDuration}</td>
								<td><c:choose>
										<c:when test="${not empty late.lateReason}">${late.lateReason}</c:when>
										<c:otherwise>미입력</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${late.status == 'confirmed'}">확인</c:when>
										<c:otherwise>미확인</c:otherwise>
									</c:choose></td>
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

<jsp:include page="../common/footer2.jsp" />
