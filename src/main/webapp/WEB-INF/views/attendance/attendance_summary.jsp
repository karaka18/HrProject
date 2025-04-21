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
<title>근무 조회</title>
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

.search-form {
	margin-bottom: 20px;
}

.search-form label {
	margin-right: 10px;
}

.search-form input,
.search-form select {
	margin-right: 15px;
	padding: 5px;
}

.summary-box {
	margin-top: 30px;
	font-size: 16px;
	background-color: #f9f9f9;
	padding: 15px;
	border: 1px solid #ccc;
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

			<!-- 검색 조건 폼 -->
			<form action="/attendance/records" method="get" class="search-form">
				<label for="startDate">시작일:</label>
				<input type="date" id="startDate" name="startDate" value="${param.startDate}" />

				<label for="endDate">종료일:</label>
				<input type="date" id="endDate" name="endDate" value="${param.endDate}" />

				<label for="category">카테고리:</label>
				<select name="category" id="category">
					<option value="">전체</option>
					<option value="야근" ${param.category eq '야근' ? 'selected' : ''}>야근</option>
					<option value="출장" ${param.category eq '출장' ? 'selected' : ''}>출장</option>
					<option value="휴일근무" ${param.category eq '휴일근무' ? 'selected' : ''}>휴일 근무</option>
				</select>

				<button type="submit">조회하기</button>
			</form>

			<!-- 근무 요약 테이블 -->
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
			<c:forEach var="record" items="${attendanceSummaryList}">
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

			<!-- 누적 정보 표시 -->
			<div class="summary-box">
				<p><strong>누적 근무 일수:</strong> ${totalWorkDays}일</p>
				<p><strong>누적 근무 시간:</strong> ${totalWorkHours}시간</p>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../common/footer2.jsp" />
