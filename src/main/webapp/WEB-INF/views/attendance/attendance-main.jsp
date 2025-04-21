<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>출퇴근 메인</title>
    <style>
        .container { display: flex; }
        .sidebar {
            width: 220px;
            background-color: #f4f4f4;
            padding: 20px;
            height: 100vh;
        }
        .main-content {
            flex-grow: 1;
            padding: 30px;
        }
        .clock-box { font-size: 18px; margin-bottom: 20px; }
        .attendance-buttons button {
            margin-right: 10px;
            padding: 10px 20px;
        }
    </style>
    <script>
        function updateClock() {
            const now = new Date();
            const timeStr = now.toLocaleTimeString('ko-KR', { hour12: false });
            document.getElementById("clock").textContent = timeStr;
        }

        function sendAttendance(type) {
            const empId = document.getElementById("empId").value;
            const url = (type === 'start') ? '/attendance/clock-in' : '/attendance/clock-out';

            fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ empId })
            }).then(res => {
                if (res.ok) {
                    alert(type === 'start' ? "출근 완료!" : "퇴근 완료!");
                    location.reload();
                } else {
                    alert("처리 실패");
                }
            });
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
            <li class="active"><a href="/attendance/main">출퇴근 현황</a></li>
            <li><a href="/attendance/records">근무 기록</a></li>
            <li><a href="/attendance/leave">휴가 내역</a></li>
            <li><a href="/attendance/status">근태 상태</a></li>
        </ul>
    </div>

    <div class="main-content">
        <h1>안녕하세요, <strong>${loginUser.emp_name}</strong>님!</h1>

        <!-- hidden empId -->
        <input type="hidden" id="empId" value="${empId}" />

        <div class="clock-box">
            현재 시간: <span id="clock" style="font-weight: bold;"></span>
        </div>

        <div class="attendance-buttons">
            <button onclick="sendAttendance('start')">출근</button>
            <button onclick="sendAttendance('end')">퇴근</button>
        </div>

        <div class="attendance-times">
            <p>오늘의 출근 시간:
                <c:choose>
                    <c:when test="${not empty todayAttendance.checkInTime}">
                        ${todayAttendance.checkInTime}
                    </c:when>
                    <c:otherwise>--:--</c:otherwise>
                </c:choose>
            </p>
            <p>오늘의 퇴근 시간:
                <c:choose>
                    <c:when test="${not empty todayAttendance.checkOutTime}">
                        ${todayAttendance.checkOutTime}
                    </c:when>
                    <c:otherwise>--:--</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>
</div>
</body>
</html>