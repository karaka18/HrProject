<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../../common/header.jsp" />
<jsp:include page="../../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="personnel" />
</jsp:include>

<fmt:formatDate value="${employee.empJd}" pattern="yyyy-MM-dd" var="empJdFormatted" />
<c:if test="${not empty employee.empQd}">
    <fmt:formatDate value="${employee.empQd}" pattern="yyyy-MM-dd" var="empQdFormatted" />
</c:if>

<div class="content">
    <h2>인사 정보 수정</h2>

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
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[readonly] {
            background-color: #f2f2f2;
            color: #777;
            cursor: not-allowed;
        }
        .btn-primary {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>

    <form action="${pageContext.request.contextPath}/admin/employee/edit" method="post" class="employee-form">
        <input type="hidden" name="empId" value="${employee.empId}" />

        <div class="form-grid">
            <label>사번:</label>
            <input type="text" value="${employee.empId}" readonly />

            <label>이름:</label>
            <input type="text" value="${employee.empName}" readonly />

            <label>성별:</label>
            <input type="text" value="${employee.empGender}" readonly />

            <label>주민번호:</label>
            <input type="text" value="${employee.empJm}" readonly />

            <label>전화번호:</label>
            <input type="text" value="${employee.empPhone}" readonly />

            <label>이메일:</label>
            <input type="text" value="${employee.empEmail}" readonly />

            <label>주소:</label>
            <input type="text" value="${employee.empAddress}" readonly />

            <label>등록자:</label>
            <input type="text" value="${employee.empRegister}" readonly />

            <label>사진:</label>
            <input type="text" value="${employee.empPht}" readonly />

            <label>회사명:</label>
            <input type="text" name="empCn" value="${employee.empCn}" />

            <label>입사일자:</label>
            <input type="date" name="empJd" value="${empJdFormatted}" />

            <label>퇴사일자:</label>
            <input type="date" name="empQd" value="${empQdFormatted}" />

            <label>부서명:</label>
            <select name="depId" class="form-control">
                <option value="10" ${employee.depId == '10' ? 'selected' : ''}>인사부서</option>
                <option value="20" ${employee.depId == '20' ? 'selected' : ''}>경영부서</option>
                <option value="30" ${employee.depId == '30' ? 'selected' : ''}>생산부서</option>
                <option value="40" ${employee.depId == '40' ? 'selected' : ''}>개발부서</option>
            </select>

            <label>직책:</label>
            <select name="posId" class="form-control">
                <option value="부장" ${employee.posId == '부장' ? 'selected' : ''}>부장</option>
                <option value="과장" ${employee.posId == '과장' ? 'selected' : ''}>과장</option>
                <option value="팀장" ${employee.posId == '팀장' ? 'selected' : ''}>팀장</option>
                <option value="대리" ${employee.posId == '대리' ? 'selected' : ''}>대리</option>
                <option value="사원" ${employee.posId == '사원' ? 'selected' : ''}>사원</option>
            </select>

            <label>직급:</label>
            <select name="rankId" class="form-control">
                <option value="1급" ${employee.rankId == '1급' ? 'selected' : ''}>1급</option>
                <option value="2급" ${employee.rankId == '2급' ? 'selected' : ''}>2급</option>
                <option value="3급" ${employee.rankId == '3급' ? 'selected' : ''}>3급</option>
                <option value="4급" ${employee.rankId == '4급' ? 'selected' : ''}>4급</option>
                <option value="5급" ${employee.rankId == '5급' ? 'selected' : ''}>5급</option>
            </select>

            <label>권한:</label>
            <select name="roleId" class="form-control">
                <option value="1" ${employee.roleId == 1 ? 'selected' : ''}>1급 관리자</option>
                <option value="2" ${employee.roleId == 2 ? 'selected' : ''}>2급 관리자</option>
                <option value="3" ${employee.roleId == 3 ? 'selected' : ''}>3급 관리자</option>
                <option value="4" ${employee.roleId == 4 ? 'selected' : ''}>일반 사원</option>
                <option value="5" ${employee.roleId == 5 ? 'selected' : ''}>일반 사용자</option>
            </select>
        </div>

        <button type="submit" class="btn-primary">수정 완료</button>
    </form>
</div>

<jsp:include page="../../common/footer.jsp" />
<script src="<c:url value='/resources/js/script.js' />"></script>
<script src="<c:url value='/resources/js/session-timer.js' />"></script>
</body>
</html>
