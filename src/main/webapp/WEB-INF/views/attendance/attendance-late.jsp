<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itwill.attendance.dto.AttendanceLateDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 공통 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/user-sidebar.jsp">
    <jsp:param name="menu" value="attendance" />
</jsp:include>
<!-- 공통 템플릿 include -->

<!-- 세션 로그인 체크 (JSTL 방식) -->
<c:if test="${empty sessionScope.id}">
    <c:redirect url="/member/login" />
</c:if>

<head>
    <!-- 스타일 시트 -->
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/user-style.css' />">

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <title>지각 현황</title>

    <script>
        function loadLateStatus() {
            const empId = $('#empId').val();
            const startDate = $('#startDate').val();
            const endDate = $('#endDate').val();

            $.ajax({
                url: "<c:url value='/attendance/late-status' />",
                type: "POST",
                data: {
                    empId: empId,
                    startDate: startDate,
                    endDate: endDate
                },
                success: function(response) {
                    const lateDetails = response.lateDetails;
                    const lateStats = response.lateStats;

                    let tableContent = '';
                    if (lateDetails && lateDetails.length > 0) {
                        $.each(lateDetails, function(index, record) {
                            tableContent += `
                                <tr>
                                    <td>${record.startDate}</td>
                                    <td>${record.latenessMinutes}</td>
                                    <td>${record.reason}</td>
                                    <td>${record.managerCheck}</td>
                                    <td>${record.fileName || '-'}</td>
                                </tr>`;
                        });
                    } else {
                        tableContent = '<tr><td colspan="5">해당 기간 내 지각 정보가 없습니다.</td></tr>';
                    }

                    $('#lateDetailsTable tbody').html(tableContent);
                    $('#totalLateCount').text(lateStats.totalLateCount || 0);
                    $('#totalLateMinutes').text(lateStats.totalLateMinutes || 0);
                },
                error: function() {
                    alert('지각 현황 조회에 실패했습니다.');
                }
            });
        }

        $(document).ready(function() {
            // 세션의 로그인 ID를 EL로 전달
            $('#empId').val('${sessionScope.id}');
            $('#fetchLateStatusBtn').on('click', loadLateStatus);
        });
    </script>
</head>

<body>
    <div class="container">
        <div class="main-content">
            <h1>지각 현황</h1>

            <!-- 지각 현황 조회 폼 -->
            <form id="lateStatusForm">
                <label for="empId">사원 ID:</label>
                <input type="text" id="empId" name="empId" readonly>
                
                <label for="startDate">조회 시작 날짜:</label>
                <input type="date" id="startDate" name="startDate">
                
                <label for="endDate">조회 종료 날짜:</label>
                <input type="date" id="endDate" name="endDate">
                
                <button type="button" id="fetchLateStatusBtn">조회하기</button>
            </form>

            <!-- 지각 통계 -->
            <div>
                <h3>지각 통계</h3>
                <p>지각 횟수: <span id="totalLateCount">0</span></p>
                <p>총 지각 시간: <span id="totalLateMinutes">0</span> 분</p>
            </div>

            <!-- 지각 내역 테이블 -->
            <table id="lateDetailsTable" border="1">
                <thead>
                    <tr>
                        <th>지각 날짜</th>
                        <th>지각 시간(분)</th>
                        <th>지각 사유</th>
                        <th>관리자 확인 상태</th>
                        <th>사유서</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- AJAX 응답으로 동적으로 출력 -->
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="../common/footer2.jsp" />
</body>