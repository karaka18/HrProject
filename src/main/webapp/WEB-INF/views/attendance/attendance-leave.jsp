<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itwill.attendance.dto.AttendanceCheckDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 공통 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/user-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>
<!-- 공통 템플릿 include -->

<%
    //세션 로그인 체크
    String empId = (String) session.getAttribute("id");
    if (empId == null) {
        response.sendRedirect(request.getContextPath() + "/member/login");  // 컨트롤러 호출 → 뷰 리졸버 통해 JSP 열림
        return;
    }
    com.itwill.approval.dto.ApprovalSearchDTO loginUser = new com.itwill.approval.dto.ApprovalSearchDTO();
    loginUser.setEmpId(empId);
%>

<head>
    <!-- 스타일 시트 -->
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/user-style.css' />">

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <title>휴가 내역 조회</title>
<style>
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
        }

        .no-data {
            color: red;
            font-weight: bold;
        }

        .report-link {
            color: blue;
            text-decoration: underline;
            cursor: pointer;
        }

        .btn {
            padding: 8px 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #45a049;
        }

        .input-date {
            padding: 6px;
            margin-right: 10px;
        }

        .search-bar {
            margin-bottom: 20px;
        }
    </style>
    <script>
        function showAlert(msg) {
            alert(msg);
        }

        window.onload = function() {
            const alertMsg = "${alertMsg}";
            if (alertMsg !== "") {
                showAlert(alertMsg);
            }
        }
    </script>
</head>
<body>
    <h2>사원 휴가 내역 조회</h2>

    <form action="${pageContext.request.contextPath}/attendance/leave/search" method="post">
        <input type="hidden" name="empId" value="${sessionScope.loginEmp.empId}" />
        
        <div class="search-bar">
            단일 날짜:
            <input type="date" name="date" class="input-date" />

            또는 기간:
            <input type="date" name="startDate" class="input-date" />
            ~
            <input type="date" name="endDate" class="input-date" />

            <button type="submit" class="btn">조회하기</button>
        </div>
    </form>

    <c:if test="${not empty leaveList}">
        <table>
            <thead>
                <tr>
                    <th>신청 일자</th>
                    <th>휴가 기간</th>
                    <th>휴가 종류</th>
                    <th>일수</th>
                    <th>결재 결과</th>
                    <th>보고서</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="leave" items="${leaveList}">
                    <tr>
                        <td><fmt:formatDate value="${leave.uploadDate}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <fmt:formatDate value="${leave.leaveStartDate}" pattern="yyyy-MM-dd"/>
                            ~
                            <fmt:formatDate value="${leave.leaveEndDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>${leave.leaveStatus}</td>
                        <td>${leave.leaveDays}일</td>
                        <td>${leave.approvalConfirmed}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/attendance/leave/report/${leave.leaveId}" class="report-link">
                                ${leave.fileName}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty leaveList && empty alertMsg}">
        <p class="no-data">휴가 내역을 조회해 주세요.</p>
    </c:if>
    
<jsp:include page="../common/footer2.jsp" />
</body>


