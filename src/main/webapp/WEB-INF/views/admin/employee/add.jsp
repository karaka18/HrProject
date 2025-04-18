<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../../common/header.jsp" />
<jsp:include page="../../common/admin-sidebar.jsp">
    <jsp:param name="menu" value="personnel" />
</jsp:include>

<div class="content">
    <h2>사원 추가</h2>

    <form action="<c:url value='/admin/employee/add' />" method="post" class="form-box">
        <div class="form-group">
            <label for="empId">사원번호</label>
            <input type="text" id="empId" name="empId" class="form-control" placeholder="사원번호 입력" required />
        </div>

        <button type="submit" class="btn btn-primary">추가</button>
        <a href="<c:url value='/admin/employee/info' />" class="btn btn-secondary">목록으로</a>
    </form>
</div>

<jsp:include page="../../common/footer.jsp" />