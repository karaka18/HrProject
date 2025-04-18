<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itwill.attendance.dto.AttendanceStatusDTO" %>
<!-- 템플릿 include -->
<!-- http://localhost:8088/attendance/attendance-main -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
<html>
<head>
    <title>근태 관리 메인 페이지</title>
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

        // 출근/퇴근 시간 기록
        function setAttendanceTime(type) {
            const now = new Date();
            const timeStr = now.toLocaleTimeString('ko-KR', { hour12: false });

            if (type === 'start') {
                document.getElementById("startTime").textContent = timeStr;
            } else {
                document.getElementById("endTime").textContent = timeStr;
            }
        }

        // 출근/퇴근 요청 함수
        function sendAttendance(type) {
            const empId = document.getElementById("empId").value; // 세션에서 empId 가져오기
            const url = (type === 'start') ? '/attendance/clock-in' : '/attendance/clock-out';

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ empId })
            })
            .then(response => {
                if (response.ok) {
                    alert(type === 'start' ? '출근 등록 완료!' : '퇴근 등록 완료!');
                    location.reload();
                } else {
                    alert('처리에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('에러 발생!');
            });
        }
    </script>
</head>
<body>
<div class="container">
    <div class="sidebar">
        <h2>근태 관리</h2>
        <ul>
            <li class="active"><a href="<c:url value='/attendance/main' />">출퇴근 기록부 및 현황</a></li>
            <li><a href="<c:url value='/attendance/late' />">지각 현황</a></li>
            <li><a href="<c:url value='/attendance/summary' />">근무 조회</a></li>
            <li><a href="<c:url value='/attendance/items' />">근태 항목</a></li>
            <li><a href="<c:url value='/attendance/leave' />">휴가 내역 확인</a></li>
        </ul>
    </div>

    <div class="main-content">
        <h1>반갑습니다, <strong>${sessionScope.loginUser.emp_name}</strong>님!</h1>

        <!-- 세션에서 empId를 hidden input에 저장 -->
        <input type="hidden" id="empId" value="${sessionScope.empId}" />

        <div class="clock-box">
            현재 시간: <span id="clock" style="font-weight: bold;"></span>
        </div>

        <div class="attendance-buttons">
            <button onclick="sendAttendance('start')">출근하기</button>
            <button onclick="sendAttendance('end')">퇴근하기</button>
        </div>

        <div class="attendance-times">
            <p>오늘의 출근 시간: <span id="startTime">--:--:--</span></p>
            <p>오늘의 퇴근 시간: <span id="endTime">--:--:--</span></p>
        </div>
    </div>
</div>
</body>
</html>
