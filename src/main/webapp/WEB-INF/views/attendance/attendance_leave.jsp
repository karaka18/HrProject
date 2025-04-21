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

        <!-- 휴가 신청서 작성 부분 추가 -->
        <div style="margin-top: 30px;">
            <h4>📥 휴가 신청서 작성</h4>
            <form id="leaveForm" method="post" action="/attendance/submit-leave" onsubmit="return validateLeave()">
                <label>휴가 시작일: <input type="date" id="startDate" name="startDate" required></label><br>
                <label>휴가 종료일: <input type="date" id="endDate" name="endDate" required></label><br>

                <button type="submit">제출하기</button>
            </form>
        </div>

        <div style="margin-top: 30px;">
            <h4>📥 휴가 신청서 다운로드</h4>
            <a href="/download/leave-form.pdf" target="_blank">PDF</a> |
            <a href="/download/leave-form.xlsx" target="_blank">Excel</a>
        </div>
    </div>
</div>

<script>
    // 남은 연차 정보 (서버에서 전달된 데이터)
    const remainingDays = ${remainingDays};  // 컨트롤러에서 모델로 넘겨준 값
    const empName = "${empName}";  // 로그인된 사원 이름

    // 휴가 신청서 유효성 검사
    function validateLeave() {
        const start = new Date(document.getElementById('startDate').value);
        const end = new Date(document.getElementById('endDate').value);

        // 신청한 연차 계산 (끝날짜 - 시작날짜 + 1)
        const requestedDays = (end - start) / (1000 * 60 * 60 * 24) + 1;

        // 연차가 남은 연차보다 많은 경우 경고
        if (requestedDays > remainingDays) {
            alert(empName + " 님의 사용 가능한 연차는 " + remainingDays + "일 입니다.");
            return false;  // 제출하지 않음
        }
        return true;  // 정상 제출
    }
</script>

</body>
<jsp:include page="../common/footer2.jsp" />
