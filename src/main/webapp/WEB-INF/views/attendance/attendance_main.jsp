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
    <title>출퇴근 기록부 및 현황</title>
    <script>
        function updateTime() {
            const currentTime = new Date();
            document.getElementById("currentTime").innerText = currentTime.toLocaleTimeString();
        }
        setInterval(updateTime, 1000); // 1초마다 시간 갱신
    </script>
</head>

<body onload="updateTime()">
    <div class="attendance_main">
        <div id="profile">
            <img src="path_to_profile_picture.jpg" alt="프로필 이미지">
            <span id="currentTime"></span>
        </div>

        <c:if test="${not empty sessionScope.loginUser}">
            <h2>반갑습니다, ${sessionScope.loginUser.empName}님!</h2>
        </c:if>

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
    </div>
</body>

<jsp:include page="../common/footer2.jsp" />
