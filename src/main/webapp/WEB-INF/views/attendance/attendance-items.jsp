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
    
     <title>근태 항목 조회</title>
</head>

<body>
	<h2>근태 항목 조회</h2>
     
     <form id="attendanceForm">
    <label>날짜: <input type="date" name="workDate" required></label><br><br>
    <label>카테고리:
        <select name="categoryAtt" required>
            <option value="">선택</option>
            <option value="근무">근무</option>
            <option value="지각">지각</option>
            <option value="결근">결근</option>
            <option value="휴가">휴가</option>
            <option value="출장">출장</option>
            <option value="야근">야근</option>
            <option value="휴일근무">휴일근무</option>
        </select>
    </label><br><br>
    <label>사원 번호: <input type="text" name="empId" required value="<%= empId %>"></label><br><br>
    <button type="submit">조회하기</button>
</form>

<br>
<div id="result" style="border: 1px solid #ccc; padding: 10px; display: none;">
    <h3>조회 결과</h3>
    <ul id="workItemList"></ul>
</div>

<script>
    // <![CDATA[
    $("#attendanceForm").submit(function (e) {
        e.preventDefault();

        const formData = {
            empId: $("input[name='empId']").val(),
            workDate: $("input[name='workDate']").val(),
            categoryAtt: $("select[name='categoryAtt']").val()
        };

        $.ajax({
            url: "/api/attendance/work-item",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (data) {
                if (data && data.workStatus !== null) {
                    const html = "<li>" +
                        "날짜: " + data.workDate + "<br>" +
                        "근태 유형: " + data.workStatus + "<br>" +
                        (data.checkInTime && data.checkOutTime
                            ? "출근: " + data.checkInTime + ", 퇴근: " + data.checkOutTime
                            : "") +
                        "</li>";
                    $("#workItemList").html(html);
                    $("#result").show();
                } else {
                    alert("해당하는 정보가 존재하지 않습니다.");
                    $("#result").hide();
                }
            },
            error: function () {
                alert("서버와 통신 중 오류가 발생했습니다.");
            }
        });
    });
    // ]]>
</script>
<jsp:include page="../common/footer2.jsp" />    
</body>