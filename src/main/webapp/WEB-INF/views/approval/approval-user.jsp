<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.itwill.approval.dto.ApprovalSearchDTO" %>
<!-- 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp">
    <jsp:param name="menu" value="approval" />
</jsp:include>

<%-- 테스트용 로그인 사용자 지정 (최아영 emp_id = 22100003) --%>
<%-- <%
	com.itwill.approval.dto.ApprovalSearchDTO loginUser = new com.itwill.approval.dto.ApprovalSearchDTO();
	loginUser.setEmpId("22100003");
	session.setAttribute("loginUser", loginUser);
%> --%>

<%
	String empId = (String) session.getAttribute("id");
	if (empId == null) {
		response.sendRedirect(request.getContextPath() + "/member/login");  // 컨트롤러 호출 → 뷰 리졸버 통해 JSP 열림
	    return;
	}
	com.itwill.approval.dto.ApprovalSearchDTO loginUser = new com.itwill.approval.dto.ApprovalSearchDTO();
	loginUser.setEmpId(empId);
%>

<!-- 본문 시작 -->
<div class="main-container">

  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  
  <style>
.main-container {
	flex: 1;
	padding: 0;
	background-color: #f9f9f9;
}

.main-container form {
	max-width: 1000px;
	margin: auto;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 0 8px rgba(0, 0, 0, 0.08);
}

@font-face {
	font-family: 'Pretendard';
	src: url('/resources/fonts/Pretendard-Regular.otf') format('opentype');
}

html, body {
	font-family: 'Pretendard', sans-serif;
	background: #f5f5f5;
}

.my-content-wrapper {
  padding: 40px;
  background-color: #f9f9f9;
}

.my-content-wrapper p {
	font-weight: bold;
}

.my-content-wrapper h2 {
	text-align: center;
	margin-bottom: 20px;
}

.my-content-wrapper table {
	width: 100%;
	border-collapse: collapse;
	background: #fff;
	box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
}

.my-content-wrapper th, td {
	border: 1px solid #ccc;
	padding: 10px;
	text-align: center;
	font-size: 0.95em;
}

.my-content-wrapper .popup, .popup-overlay {
	display: none;
	position: fixed;
	z-index: 1000;
}

.my-content-wrapper .popup-overlay {
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}

.my-content-wrapper .popup {
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background: #fff;
	width: 600px;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.my-content-wrapper .popup .close-btn {
	position: absolute;
	top: 10px;
	right: 15px;
	cursor: pointer;
}

.my-content-wrapper .comment-box {
	margin-top: 20px;
	background: #f9f9f9;
	padding: 10px;
	border-radius: 6px;
}

.my-content-wrapper #pagination {
	margin-top: 25px;
	text-align: center;
}

.my-content-wrapper #pagination button {
	margin: 0 5px;
	padding: 5px 12px;
	border: 1px solid #007bff;
	background-color: white;
	color: #007bff;
	border-radius: 5px;
	cursor: pointer;
	font-size: 0.95em;
}

.my-content-wrapper #pagination button.active {
	background-color: #007bff;
	color: white;
	font-weight: bold;
}

.my-content-wrapper #pagination button:hover:not (.active ) {
	background-color: #f0f8ff;
}

.my-content-wrapper button {
	padding: 7px 16px;
	font-size: 15px;
	background: #f0f0f0;
	color: black;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

#popupContent {
  line-height: 1.8; /* 줄 간격 */
  font-size: 15px;
}

#popupContent h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

#popupContent p {
  margin: 6px 0;
}

#popupContent ul {
  margin: 6px 0 10px 20px;
  padding: 0;
}

#popupContent a {
  color: #007bff;
  text-decoration: underline;
}

#popupContent strong {
  display: inline-block;
  margin-top: 10px;
  font-weight: bold;
}

.status {
  font-weight: bold;
}

.status.approved {
  color: blue;
}

.status.rejected {
  color: orange;
}

.status.pending {
  color: gray;
}
</style>

<div class="my-content-wrapper">
  <h2>내 결재 진행 상황</h2>
  <table id="userApprovalTable">
    <thead>
      <tr>
        <th>순번</th>
        <th>요청일</th>
        <th>제목</th>
        <th>상세내용</th>
        <th>처리상태</th>
      </tr>
    </thead>
    <tbody id="userApprovalBody"></tbody>
  </table>
  <div id="pagination"></div>

  <!-- 팝업 -->
  <div class="popup-overlay" id="popupOverlay"></div>
  <div class="popup" id="popupWindow">
    <span class="close-btn" onclick="closePopup()">✖</span>
    <div id="popupContent">상세 정보 로딩 중...</div>
  </div>

  <script>
  const loginUserId = "<%= empId %>";
    let approvalList = [];
    let itemsPerPage = 10;

    function loadUserApprovals() {
      $.ajax({
        url: '/approval/user-documents',
        method: 'GET',
        data: { requesterId: loginUserId },
        success: function(data) {
          approvalList = data;
          renderUserPage(1);
        }
      });
    }

    function renderUserPage(page) {
    	  const tbody = $('#userApprovalBody');
    	  tbody.empty();

    	  const start = (page - 1) * itemsPerPage;
    	  const end = start + itemsPerPage;
    	  const sliced = approvalList.slice(start, end);

    	  sliced.forEach((item, idx) => {
    	    const rawStatus = item.approvalStatus?.trim(); // null, undefined, 공백까지 처리
    	    const status = (!rawStatus || rawStatus === '') ? '대기' : rawStatus;

    	    const statusColor =
    	      status === '승인' ? 'blue' :
    	      status === '반려' ? 'orange' : 'gray';

    	    const row = '<tr>' +
    	      '<td>' + (start + idx + 1) + '</td>' +
    	      '<td>' + item.requestDate + '</td>' +
    	      '<td>' + item.documentTitle + '</td>' +
    	      '<td><button onclick="openPopup(\'' + item.approvalDocumentId + '\')">상세확인</button></td>' +
    	      '<td style="color:' + statusColor + '; font-weight: bold;">' + status + '</td>' +
    	      '</tr>';

    	    tbody.append(row);
    	  });

    	  renderPagination(page);
    	}

    function renderPagination(current) {
      const totalPages = Math.ceil(approvalList.length / itemsPerPage);
      const container = $('#pagination');
      container.empty();

      for (let i = 1; i <= totalPages; i++) {
        const btn = $('<button>').text(i).click(() => renderUserPage(i));
        if (i === current) btn.css({ background: '#007bff', color: 'white' });
        container.append(btn);
      }
    }

    function openPopup(documentId) {
      $.ajax({
        url: '/approval/user-detail?documentId=' + documentId,
        method: 'GET',
        success: function(detail) {
          let html = '';
          html += '<h3>[' + detail.documentTitle + ']</h3>';
          html += '<p>요청일: ' + detail.formattedRequestDate + '</p>';
          html += '<p>요청 코멘트: ' + (detail.comment || '-') + '</p>';

          if (detail.referenceTableName === 'LEAVE') {
            html += '<p>휴가 구분: ' + detail.leaveStatus + '</p>';
            html += '<p>기간: ' + detail.formattedLeaveStartDate + ' ~ ' + detail.formattedLeaveEndDate + ' (' + detail.leaveDays + '일)</p>';
          } else if (detail.referenceTableName === 'BUSINESS') {
            html += '<p>출장지: ' + detail.tripLocation + '</p>';
            html += '<p>출장 목적: ' + detail.businessTripPurpose + '</p>';
            html += '<p>기간: ' + detail.formattedBusinessTripStart + ' ~ ' + detail.formattedBusinessTripEnd + ' (' + detail.tripDays + '일)</p>';
          }

          if (detail.files && detail.files.length > 0) {
            html += '<p>첨부파일:</p><ul>';
            detail.files.forEach(file => {
            	html += '<li><a href="/approval/download?fileId=' + file.fileId + '">' + file.fileName + '</a></li>';
            });
            html += '</ul>';
          }
		
          if (detail.approvers && detail.approvers.length > 0) {
        	  html += '<p><strong>결재선:</strong><br>';

        	  const approverLine = detail.approvers.map((a, idx) => {
        	    const status = a.status || '대기';
        	    const statusClass =
        	      status === '승인' ? 'approved' :
        	      status === '반려' ? 'rejected' : 'pending';

        	    return (
        	      (idx + 1) + '. ' +
        	      a.name + ' (' + a.dept + ' / ' + a.position + ') ' +
        	      '<span class="status ' + statusClass + '">(' + status + ')</span>'
        	    );
        	  }).join(' → ');

        	  html += approverLine + '</p>';
        	}
          
          if (detail.approverComments && detail.approverComments.length > 0) {
            html += '<div class="comment-box">';
            html += '<h4>&lt;결재 코멘트&gt;</h4>';
            html += '<ul>';
            detail.approverComments.forEach(c => {
              if (c.comment) {
                html += '<li><b> ' + c.empName + '</b> : ' + c.comment + '</li>';
              }
            });
            html += '</ul></div>';
          }

          $('#popupContent').html(html);
          $('#popupOverlay').show();
          $('#popupWindow').show();
        }
      });
    }

    function closePopup() {
      $('#popupOverlay').hide();
      $('#popupWindow').hide();
    }

    $(document).ready(function() {
      loadUserApprovals();
    });
  </script>
  </div>
  </div>
  <jsp:include page="../common/footer.jsp" />