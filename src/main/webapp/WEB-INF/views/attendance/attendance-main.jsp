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

    <title>출퇴근 기록부 및 현황</title>

    <script>
        $(document).ready(function () {
            // 출근 처리
            $('#checkInBtn').click(function (event) {
                event.preventDefault();
                var empId = $('#empId').val();

                $.ajax({
                    type: 'POST',
                    url: '/attendance/check-in',
                    data: { empId: empId },
                    success: function (response) {
                        $('#statusMessage').text("출근 완료!");
                        $('#checkInTime').text(response.checkInTime);
                    },
                    error: function () {
                        $('#statusMessage').text("출근 처리에 실패했습니다. 다시 시도해주세요.");
                    }
                });
            });

            // 퇴근 처리
            $('#checkOutBtn').click(function (event) {
                event.preventDefault();
                var empId = $('#empId').val();

                $.ajax({
                    type: 'POST',
                    url: '/attendance/check-out',
                    data: { empId: empId },
                    success: function (response) {
                        $('#statusMessage').text("퇴근 완료!");
                        $('#checkOutTime').text(response.checkOutTime);
                    },
                    error: function () {
                        $('#statusMessage').text("퇴근 처리에 실패했습니다. 다시 시도해주세요.");
                    }
                });
            });

            // 특정 날짜 출퇴근 기록 조회
            $("#check-attendance-form").submit(function (event) {
                event.preventDefault();
                var empId = $("input[name='empId']").val();
                var workDate = $("input[name='workDate']").val();

                $.ajax({
                    url: "/attendance/check-attendance",
                    type: "POST",
                    data: { empId: empId, workDate: workDate },
                    success: function (response) {
                        if (response.attendance) {
                            alert(response.message);
                            console.log(response.attendance);
                        } else {
                            alert(response.message);
                        }
                    },
                    error: function () {
                        alert("출퇴근 기록 조회에 실패했습니다.");
                    }
                });
            });
        });
    </script>
</head>

<body>
    <h1>출퇴근 메인 페이지</h1>
    <p>정상적으로 jsp 연결됨!</p>
    <!-- 출근 버튼 -->
    <form>
        <input type="hidden" id="empId" value="${empId}" />
        <button type="button" id="checkInBtn">출근하기</button>
    </form>

    <!-- 퇴근 버튼 -->
    <form>
        <input type="hidden" id="empId" value="${empId}" />
        <button type="button" id="checkOutBtn">퇴근하기</button>
    </form>

    <!-- 상태 메시지 및 시간 출력 -->
    <div id="statusMessage"></div>
    <div>출근 시간: <span id="checkInTime"></span></div>
    <div>퇴근 시간: <span id="checkOutTime"></span></div>

    <!-- 출퇴근 기록 조회 -->
    <form id="check-attendance-form">
        <input type="text" name="empId" placeholder="사원 ID" />
        <input type="date" name="workDate" />
        <button type="submit">출퇴근 기록 조회</button>
    </form>

<jsp:include page="../common/footer2.jsp" />
</body>