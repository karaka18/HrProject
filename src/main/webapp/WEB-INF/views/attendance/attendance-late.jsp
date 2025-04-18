<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itwill.attendance.dto.LatenessDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 템플릿 include -->
<!-- http://localhost:8088/attendance/attendance-late -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
<html>
<head>
    <title>지각 현황</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
    <script>
        // 현재 시간 실시간 표시
        function updateClock() {
            const now = new Date();
            const timeStr = now.toLocaleTimeString('ko-KR', { hour12: false });
            document.getElementById("clock").textContent = timeStr;
        }

        setInterval(updateClock, 1000);
        window.onload = updateClock;
    </script>
</head>
<body>
<div class="container">
    <div class="sidebar">
        <h2>근태 관리</h2>
        <ul>
            <li><a href="<c:url value='/attendance/main' />">출퇴근 기록부 및 현황</a></li>
            <li class="active"><a href="<c:url value='/attendance/late' />">지각 현황</a></li>
            <li><a href="<c:url value='/attendance/summary' />">근무 조회</a></li>
            <li><a href="<c:url value='/attendance/items' />">근태 항목</a></li>
            <li><a href="<c:url value='/attendance/leave' />">휴가 내역 확인</a></li>
        </ul>
    </div>

    <div class="main-content">
        <h1>지각 현황</h1>
        <p>다음은 사용자의 지각 내역입니다:</p>

        <!-- 날짜별로 지각 내역을 출력 -->
        <table border="1">
            <thead>
                <tr>
                    <th>지각 날짜</th>
                    <th>출근 시간</th>
                    <th>지각 사유</th>
                    <th>관리자 확인 상태</th>
                    <th>지각 시간(분)</th>
                </tr>
            </thead>
            <tbody>
                <!-- 사용자의 지각 내역을 반복문으로 출력 -->
                <c:forEach var="lateRecord" items="${attendanceLateList}">
                    <tr>
                        <td>${lateRecord.date}</td>
                        <td>${lateRecord.arrivalTime}</td>
                        <td>${lateRecord.reason}</td>
                        <td>${lateRecord.approvalStatus}</td>
                        <td>${lateRecord.lateMinutes}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
