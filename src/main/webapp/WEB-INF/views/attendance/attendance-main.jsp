<!-- attendance/main.jsp -->
<html>
<head>
    <title>출퇴근 메인 페이지</title>
    <script>
        function updateTime() {
            const currentTime = new Date();
            document.getElementById("currentTime").innerText = currentTime.toLocaleTimeString();
        }
        setInterval(updateTime, 1000); // 1초마다 시간 갱신
    </script>
</head>
<body onload="updateTime()">
    <div id="profile">
        <img src="path_to_profile_picture.jpg" alt="프로필 이미지">
        <span id="currentTime"></span>
    </div>

    <h1>출퇴근 기록</h1>

    <c:if test="${not empty attendanceDetail}">
        <table>
            <tr>
                <th>오늘의 출근 시간</th>
                <td>${attendanceDetail.checkInTime}</td>
            </tr>
            <tr>
                <th>오늘의 퇴근 시간</th>
                <td>${attendanceDetail.checkOutTime}</td>
            </tr>
        </table>
    </c:if>

    <form action="/attendance/checkIn" method="get">
        <input type="hidden" name="empId" value="${attendanceDetail.empId}">
        <button type="submit">출근하기</button>
    </form>

    <form action="/attendance/checkOut" method="get">
        <input type="hidden" name="empId" value="${attendanceDetail.empId}">
        <button type="submit">퇴근하기</button>
    </form>
</body>
</html>
