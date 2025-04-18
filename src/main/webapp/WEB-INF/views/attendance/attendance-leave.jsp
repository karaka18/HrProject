<!-- 템플릿 include -->
<!-- http://localhost:8088/attendance/attendance-leave -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>휴가 내역 조회</title>
</head>
<body>
    <h2> 휴가 내역 조회</h2>

    <form action="/attendance/leave" method="get">
        시작일: <input type="date" name="startDate" required>
        종료일: <input type="date" name="endDate" required>
        <button type="submit">조회</button>
    </form>

    <hr>

    <c:if test="${not empty leaveList}">
        <table border="1" cellpadding="8">
            <tr>
                <th>신청 일자</th>
                <th>휴가 기간</th>
                <th>보고서</th>
                <th>결재 결과</th>
            </tr>
            <c:forEach var="leave" items="${leaveList}">
                <tr>
                    <td>${leave.requestDate}</td>
                    <td>${leave.leaveStart} ~ ${leave.leaveEnd}</td>
                    <td>${leave.leaveReport}</td>
                    <td>${leave.approvalStatus}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <hr>

    <h3>휴가 신청서 다운로드</h3>
    <a href="/download/leave-form.pdf" target="_blank">PDF 다운로드</a> |
    <a href="/download/leave-form.xlsx" target="_blank">엑셀 다운로드</a>

</body>
</html>
