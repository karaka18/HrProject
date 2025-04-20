<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>근태 항목 조회</title>
    <style>
        table {
            width: 80%;
            border-collapse: collapse;
            margin: 20px auto;
        }
        th, td {
            padding: 8px 12px;
            border: 1px solid #ddd;
            text-align: center;
        }
        form {
            width: 80%;
            margin: 20px auto;
            text-align: center;
        }
    </style>
</head>
<body>

<h2 style="text-align:center;">근태 항목 조회</h2>

<form action="${pageContext.request.contextPath}/attendance/list" method="get">
    <label>년: <input type="number" name="year" value="${param.year}" /></label>
    <label>월: <input type="number" name="month" value="${param.month}" /></label>
    <label>일: <input type="number" name="day" value="${param.day}" /></label>

    <label>근무 형태:
        <select name="status">
            <option value="">--전체--</option>
            <option value="근무" ${param.status == '근무' ? 'selected' : ''}>근무</option>
            <option value="지각" ${param.status == '지각' ? 'selected' : ''}>지각</option>
            <option value="결근" ${param.status == '결근' ? 'selected' : ''}>결근</option>
            <option value="휴가" ${param.status == '휴가' ? 'selected' : ''}>휴가</option>
            <option value="출장" ${param.status == '출장' ? 'selected' : ''}>출장</option>
        </select>
    </label>

    <button type="submit">조회하기</button>
</form>

<c:if test="${not empty attendanceList}">
    <table>
        <thead>
            <tr>
                <th>날짜</th>
                <th>기간</th>
                <th>근무 형태</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="record" items="${attendanceList}">
                <tr>
                    <td>${record.date}</td>
                    <td>
                        <c:choose>
                            <c:when test="${record.status == '휴가' || record.status == '출장'}">
                                ${record.startDate} ~ ${record.endDate}
                            </c:when>
                            <c:when test="${not empty record.startTime && not empty record.endTime}">
                                ${record.startTime} ~ ${record.endTime}
                            </c:when>
                            <c:otherwise>
                                -
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${record.status}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${empty attendanceList}">
    <p style="text-align:center;">조회 결과가 없습니다.</p>
</c:if>

</body>
</html>
