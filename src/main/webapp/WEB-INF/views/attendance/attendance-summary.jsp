<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itwill.attendance.dto.AttendanceStatusDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>근무 조회</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
    <style>
        .container { display: flex; }
        .sidebar {
            width: 220px;
            background-color: #f0f0f0;
            padding: 20px;
            height: 100vh;
        }
        .sidebar h2 { margin-bottom: 20px; }
        .sidebar ul { list-style: none; padding-left: 0; }
        .sidebar ul li { margin-bottom: 10px; }
        .sidebar ul li.active a { font-weight: bold; }
        .main-content {
            flex-grow: 1;
            padding: 30px;
        }
        .page-title {
            text-align: center;
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 30px;
        }
        .clock-box {
            font-size: 18px;
            margin-bottom: 20px;
        }
        .attendance-buttons button {
            margin-right: 10px;
            padding: 10px 20px;
        }
        .section-title {
            font-size: 20px;
            margin-top: 30px;
            margin-bottom: 10px;
            font-weight: bold;
        }
    </style>
    <script>
    function updateClock() {
        const now = new Date();
        const timeStr = now.toLocaleTimeString('ko-KR', { hour12: false });
        document.getElementById("clock").textContent = timeStr;
    }

    // 시계 1초마다 업데이트
    setInterval(updateClock, 1000);
    window.onload = updateClock;

    function sendAttendance(type) {
        const empId = document.getElementById("empId").value;
        const url = (type === 'start') ? '/attendance/clock-in' : '/attendance/clock-out';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "empId": empId })
        })
        .then(function(response) {
            if (response.ok) {
                alert(type === 'start' ? '출근 등록 완료!' : '퇴근 등록 완료!');
                location.reload();
            } else {
                alert('처리에 실패했습니다.');
            }
        })
        .catch(function(error) {
            console.error('Error:', error);
            alert('에러 발생!');
        });
    }
    </script>
</head>
<body>
<div class="container">
    <!-- 전체 메뉴바 -->
    <div class="sidebar">
        <h2>메뉴</h2>
        <ul>
            <li><a href="#">인사관리</a></li>
            <li>
                <a href="#">근태관리</a>
                <ul>
                    <li><a href="<c:url value='/attendance/main' />">출퇴근 기록부 및 현황</a></li>
                    <li><a href="<c:url value='/attendance/late' />">지각 현황</a></li>
                    <li class="active"><a href="<c:url value='/attendance/summary' />">근무 조회</a></li>
                    <li><a href="<c:url value='/attendance/items' />">근태 항목</a></li>
                    <li><a href="<c:url value='/attendance/leave' />">휴가 내역 확인</a></li>
                </ul>
            </li>
            <li><a href="#">급여관리</a></li>
            <li><a href="#">전자결재 내역</a></li>
        </ul>
    </div>

    <!-- 본문 영역 -->
    <div class="main-content">
    
    <h2 style="text-align: center; margin-top: 30px;">근무 조회</h2>

<table border="1" class="attendance-table">
    <thead>
        <tr>
            <th>날짜</th>
            <th>부서명</th>
            <th>사원명</th>
            <th>출근 시간</th>
            <th>퇴근 시간</th>
            <th>근태 상태</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${attendanceList}">
            <tr>
                <td>${item.attendance_date}</td>
                <td>${item.dep_name}</td>
                <td>${item.emp_name}</td>
                <td>${item.clock_in_time}</td>
                <td>${item.clock_out_time}</td>
                <td>${item.attendance_status}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
    
    
        <div class="page-title">근무 조회</div>

        <h3>반갑습니다, <strong>${sessionScope.loginUser.emp_name}</strong>님!</h3>
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

        <!-- 여기에 근무 조회 관련 데이터 출력 테이블 또는 차트 추가 예정 -->
        <div class="section-title">[예정] 근무 이력 조회 결과</div>
        <p>데이터 조회 기능과 연동하여 근무 요약 테이블이 여기에 표시됩니다.</p>
    </div>
</div>
</body>
</html>
