<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../../common/header.jsp" />
<jsp:include page="../../common/user-sidebar.jsp">
    <jsp:param name="menu" value="personnel" />
</jsp:include>

<fmt:formatDate value="${employee.empJd}" pattern="yyyy-MM-dd" var="empJdFormatted" />

<head>
    <meta charset="UTF-8">
    <title>내 정보 수정</title>
    <style>
        .employee-form {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .form-grid {
            display: grid;
            grid-template-columns: 150px 1fr;
            gap: 10px 20px;
        }
        label {
            font-weight: bold;
        }
        input {
            padding: 6px;
            width: 100%;
        }
        input[readonly] {
            background-color: #f2f2f2;
            color: #777;
            cursor: not-allowed;
        }
        
        .btn-primary {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>

<div class="content">
    <h2>내 정보 수정</h2>

    <form action="${pageContext.request.contextPath}/user/employee/update" method="post" class="employee-form">
        <!-- ✅ Hidden 필드들 -->
        <input type="hidden" name="empId" value="${employee.empId}" />
        <input type="hidden" name="depId" value="${employee.depId}" />
        <input type="hidden" name="depName" value="${employee.depName}" />
        <input type="hidden" name="empModifier" value="${sessionScope.user.empId}" />

        <div class="form-grid">
            <!-- ❌ 수정 불가 -->
            <label>사번:</label>
            <input type="text" value="${employee.empId}" readonly />

            <label>이름:</label>
            <input type="text" value="${employee.empName}" readonly />

            <label>부서:</label>
            <input type="text" value="${employee.depName}" readonly />

            <label>입사일자:</label>
            <input type="text" value="${empJdFormatted}" readonly />

            <!-- ✅ 수정 가능 -->
            <label>전화번호:</label>
            <input type="text" name="empPhone" value="${employee.empPhone}" />

            <label>이메일:</label>
            <input type="text" name="empEmail" value="${employee.empEmail}" />

            <label>주소:</label>
            <input type="text" name="empAddress" value="${employee.empAddress}" />

            <label>사진 경로:</label>
            <input type="text" name="empPht" value="${employee.empPht}" />
            
            <!-- 비밀번호 수정 -->
            <label>새 비밀번호:</label>
            <input type="password" name="newPassword" placeholder="새 비밀번호 (8~16자)" />

            <label>비밀번호 확인:</label>
            <input type="password" name="confirmPassword" placeholder="비밀번호 확인" />
        </div>

        <button type="submit" class="btn-primary">수정 완료</button>
    </form>
</div>

<script>
    document.getElementById("employeeForm").addEventListener("submit", function(e) {
        e.preventDefault();

        const newPassword = document.querySelector("input[name='newPassword']").value;
        const confirmPassword = document.querySelector("input[name='confirmPassword']").value;

        if (newPassword !== confirmPassword) {
            alert("새 비밀번호가 일치하지 않습니다.");
            return;
        }

        const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,16}$/;
        if (!pwRegex.test(newPassword)) {
            alert("비밀번호는 8~16자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.");
            return;
        }

        // 하나의 폼에서 두 가지 작업을 처리
        const formData = new FormData(document.getElementById("employeeForm"));
        
        fetch("${pageContext.request.contextPath}/user/employee/update", {
            method: "POST",
            body: formData
        }).then(response => response.json())
          .then(data => {
              if (data.success) {
                  alert("정보 수정 완료");
              } else {
                  alert("정보 수정 실패");
              }
          }).catch(error => alert("오류 발생"));
    });
</script>



<jsp:include page="../../common/footer.jsp" />

<script src="<c:url value='/resources/js/script.js' />"></script>
<script src="<c:url value='/resources/js/session-timer.js' />"></script>
</body>
</html>
