<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>휴가 내역 조회</title>
    <style>
        .container { display: flex; }
        .sidebar {
            width: 220px;
            background-color: #f0f0f0;
            padding: 20px;
            height: 100vh;
        }
        .main-content {
            flex-grow: 1;
            padding: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #999;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        .page-title {
            font-size: 28px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="sidebar">
        <h2>근태 메뉴</h2>
        <ul>
            <li><a href="/attendance/main">출퇴근 현황</a></li>
            <li><a href="/attendance/records">근무 기록</a></li>
            <li><a href="/attendance/attendance-late">지각 내역</a></li>
            <li class="active"><a href="/attendance/leave">휴가 내역</a></li>
        </ul>
    </div>

    <div class="main-content">
        <div class="page-title">휴가 내역 조회</div>

        <form method="get" action="/attendance/attendance-leave">
            <label>시작일: <input type="date" name="startDate" required></label>
            <label>종료일: <input type="date" name="endDate" required></label>
            <button type="submit">조회</button>
        </form>

        <c:if test="${not empty leaveList}">
            <table>
                <thead>
                    <tr>
                        <th>신청 일자</th>
                        <th>휴가 기간</th>
                        <th>휴가 유형</th>
                        <th>결재 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="leave" items="${leaveList}">
                        <tr>
                            <td>${leave.requestDate}</td>
                            <td>${leave.startDate} ~ ${leave.endDate}</td>
                            <td>${leave.leaveType}</td>
                            <td>${leave.approvalStatus}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:if test="${empty leaveList}">
            <p>조회된 휴가 내역이 없습니다.</p>
        </c:if>

        <div style="margin-top: 30px;">
            <h4>📥 휴가 신청서 다운로드</h4>
            <a href="/download/leave-form.pdf" target="_blank">PDF</a> |
            <a href="/download/leave-form.xlsx" target="_blank">Excel</a>
        </div>
    </div>
</div>
</body>
</html>