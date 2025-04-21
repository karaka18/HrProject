<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>

<head>
    <meta charset="UTF-8">
    <title>근태 항목 조회</title>
</head>
<body>
    <h1>근태 항목 조회</h1>

    <!-- 검색 폼 -->
    <form action="/attendance/search" method="get">
        <label for="searchDate">날짜:</label>
        <input type="date" id="searchDate" name="searchDate" 
               value="${param.searchDate != null ? param.searchDate : ''}" required>

        <label for="category">카테고리:</label>
        <select name="category" id="category">
            <option value="" <c:if test="${empty param.category}">selected</c:if>>-- 선택 --</option>
            <option value="근무" <c:if test="${param.category == '근무'}">selected</c:if>>근무</option>
            <option value="지각" <c:if test="${param.category == '지각'}">selected</c:if>>지각</option>
            <option value="결근" <c:if test="${param.category == '결근'}">selected</c:if>>결근</option>
            <option value="휴가" <c:if test="${param.category == '휴가'}">selected</c:if>>휴가</option>
            <option value="출장" <c:if test="${param.category == '출장'}">selected</c:if>>출장</option>
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
                    <th>근무 형태</th>
                    <th>날짜</th>
                    <th>기간</th>
                    <th>지각 사유</th>
                    <th>결근 사유</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="attendance" items="${attendanceList}">
                    <tr>
                        <td>${attendance.empName}</td>
                        <td>${attendance.departmentName}</td>
                        <td>${attendance.workType}</td>
                        <td><fmt:formatDate value="${attendance.date}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${attendance.workType == '휴가' || attendance.workType == '출장'}">
                                    ${attendance.period}
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:out value="${attendance.lateReason != null ? attendance.lateReason : 'N/A'}"/>
                        </td>
                        <td>
                            <c:out value="${attendance.absenceReason != null ? attendance.absenceReason : 'N/A'}"/>
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

<jsp:include page="../common/footer2.jsp" />
