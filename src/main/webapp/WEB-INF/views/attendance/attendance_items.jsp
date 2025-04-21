<!-- attendance/items.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>근태 항목 조회</title>
</head>
<body>
    <h1>근태 항목 조회</h1>

    <!-- 검색 폼 -->
    <form action="/attendance/search" method="get">
        <label for="year">년:</label>
        <input type="number" id="year" name="year" required>
        
        <label for="month">월:</label>
        <input type="number" id="month" name="month" required>
        
        <label for="day">일:</label>
        <input type="number" id="day" name="day" required>

        <label for="category">카테고리:</label>
        <select name="category" id="category">
            <option value="근무">근무</option>
            <option value="지각">지각</option>
            <option value="결근">결근</option>
            <option value="휴가">휴가</option>
            <option value="출장">출장</option>
        </select>

        <button type="submit">조회하기</button>
    </form>

    <hr>

    <!-- 조회된 결과 -->
    <c:if test="${not empty attendanceList}">
        <h2>조회된 근태 항목</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>사원명</th>
                    <th>부서명</th>
                    <th>근태 형태</th>
                    <th>날짜</th>
                    <th>기간</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="attendance" items="${attendanceList}">
                    <tr>
                        <td>${attendance.empName}</td>
                        <td>${attendance.depName}</td>
                        <td>${attendance.status}</td>
                        <td>${attendance.date}</td>
                        <td>
                            <c:choose>
                                <c:when test="${attendance.status == '휴가' || attendance.status == '출장'}">
                                    ${attendance.startDate} ~ ${attendance.endDate}
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- 조회 결과가 없을 경우 -->
    <c:if test="${empty attendanceList}">
        <p>조회된 결과가 없습니다.</p>
    </c:if>
</body>
</html>
<jsp:include page="../common/footer2.jsp" />