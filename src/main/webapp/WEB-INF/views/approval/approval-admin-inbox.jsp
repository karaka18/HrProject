<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.itwill.approval.dto.ApprovalSearchDTO"%>
<%@ page import="com.itwill.salary.dto.SalaryEmployeeDTO" %>

<!-- 템플릿 include -->
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/sidebar.jsp">
    <jsp:param name="menu" value="approval" />
</jsp:include>

<%-- <%
	// 테스트용 로그인 사용자 지정 (이민준 emp_id = 15100002, 박지원 emp_id = 10100001)
	ApprovalSearchDTO loginUser = new ApprovalSearchDTO();
	loginUser.setEmpId("15100002"); // 박지원 테스트용
	//loginUser.setEmpId("10100001"); // 이민준 테스트용
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
}

.my-content-wrapper {
	padding: 40px;
	background-color: #f9f9f9;
}

/* 페이지네이션 스타일 */
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

.my-content-wrapper #pagination button:hover:not(.active) {
  background-color: #f0f8ff;
}

.my-content-wrapper body {
	font-family: "Noto Sans KR", sans-serif;
	margin: 30px;
	background: #f5f5f5;
}

.my-content-wrapper h2 {
	text-align: center;
	margin-bottom: 30px;
	font-size: 24px;
	font-weight: 600;
}

.my-content-wrapper table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
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
	font-size: 1.2em;
}

.my-content-wrapper #popupCommentBox {
	margin-top: 20px;
}

.my-content-wrapper #popupCommentBox textarea {
	width: 100%;
	height: 80px;
	resize: vertical;
	padding: 10px;
	margin : 5px;
	font-size: 14px;
	line-height: 1.6;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 5px;
}

.my-content-wrapper #popupWindow button {
	padding: 7px 15px;
	margin-left: 10px;
}

.my-content-wrapper #noDataMsg {
	text-align: center;
	color: #999;
	margin-top: 40px;
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
</style>

<div class="my-content-wrapper">
  <h2>결재 승인 및 반려 처리</h2>

  <table id="approvalTable">
    <thead>
      <tr>
        <th>순번</th>
        <th>요청일</th>
        <th>요청자</th>
        <th>제목</th>
        <th>상세내용</th>
		<th>처리상태</th>
      </tr>
    </thead>
    <tbody id="approvalBody">
      <!-- AJAX로 채움 -->
    </tbody>
  </table>
  <div id="pagination"></div>
  <p id="noDataMsg" style="display:none;">현재 처리할 결재 건이 없습니다.</p>

  <!-- 팝업 -->
  <div class="popup-overlay" id="popupOverlay"></div>
  <div class="popup" id="popupWindow">
    <span class="close-btn" onclick="closePopup()">✖</span>
    <div id="popupContent">상세 정보 로딩 중...</div>

    <div id="popupCommentBox">
      <label>결재 코멘트:</label>
      <textarea id="approvalComment"></textarea>
    </div>

    <div style="text-align: right; margin-top: 15px;">
      <button onclick="approve()" style="background-color: #007bff; color: white; border: none; padding: 8px 16px; border-radius: 5px;">승인</button>
	  <button onclick="reject()" style="background-color: orange; color: white; border: none; padding: 8px 16px; border-radius: 5px;">반려</button>
    </div>
  </div>

	<script>
	const loginUserId = "<%= empId %>";
		let currentDocId = '';
		let currentLineId = '';
		// 페이지네이션 관련 전역 변수 추가
		let approvalData = [];       // 전체 결재 데이터 저장
		let itemsPerPage = 10;       // 한 페이지에 표시할 항목 수
		
		function renderStatus(status) {
			const statusClass = status === '승인' 
			    ? 'color:blue;' 
			    : status === '반려' 
			    ? 'color:orange;' 
			    : 'color:gray;';
		}
		
		function loadPendingList() {
		    $.ajax({
		        url: '/approval/inbox?empId=' + loginUserId,
		        method: 'GET',
		        	success: function(data) {
	        		  const noDataMsg = $('#noDataMsg');
	        		  const table = $('#approvalTable');

	        		  // 데이터 저장
	        		  approvalData = data;

	        		  // 테이블 초기화
	        		  $('#approvalBody').empty();

	        		  if (!data || data.length === 0) {
	        		      table.hide();
	        		      noDataMsg.show();
	        		      return;
	        		  }

	        		  table.show();
	        		  noDataMsg.hide();

	        		  // 1페이지부터 시작
	        		  renderPage(1);
	        		}
	    	});
		}

		function openPopup(docId, lineId, status) {
		    currentDocId = docId;
		    currentLineId = lineId;

		    $.ajax({
		        url: '/approval/detail?documentId=' + docId,
		        method: 'GET',
		        success: function(html) {
		            $('#popupContent').html(html);
		            $('#popupOverlay').show();
		            $('#popupWindow').show();

		            // 상태가 이미 처리된 경우 버튼과 코멘트 숨기기
		            if (status === '승인' || status === '반려') {
		                $('#popupCommentBox').hide();
		                $('#popupWindow button:contains("승인")').hide();
		                $('#popupWindow button:contains("반려")').hide();
		            } else {
		                $('#popupCommentBox').show();
		                $('#popupWindow button:contains("승인")').show();
		                $('#popupWindow button:contains("반려")').show();
		            }
		        }
		    });
		}

		function closePopup() {
			$('#popupOverlay').hide();
			$('#popupWindow').hide();
		}

		function approve() {
		    const comment = $('#approvalComment').val();
		    $.ajax({
		        url : '/approval/approve',
		        method : 'POST',
		        contentType : 'application/json',
		        data : JSON.stringify({
		            approvalLineId : currentLineId,
		            approvalDocumentId : currentDocId,
		            approverId : loginUserId,
		            comment : comment
		        }),
		        success : function() {
		            alert('결재가 승인되었습니다.');
		            closePopup();
		            loadPendingList(); // 리스트 다시 로딩 (상태 표시 반영됨)
		        }
		    });
		}

		function reject() {
		    const comment = $('#approvalComment').val();
		    $.ajax({
		        url : '/approval/reject',
		        method : 'POST',
		        contentType : 'application/json',
		        data : JSON.stringify({
		            approvalLineId : currentLineId,
		            approvalDocumentId : currentDocId,
		            approverId : loginUserId,
		            comment : comment
		        }),
		        success : function() {
		            alert('결재가 반려되었습니다.');
		            closePopup();
		            loadPendingList();
		        }
		    });
		}
		
		function renderPage(pageNum) {
		    const tbody = $('#approvalBody');
		    const noDataMsg = $('#noDataMsg');

		    tbody.empty(); // 기존 행 비우기

		    const itemsPerPage = 10;
		    const startIndex = (pageNum - 1) * itemsPerPage;
		    const endIndex = startIndex + itemsPerPage;

		    const pageData = approvalData.slice(startIndex, endIndex); // 현재 페이지에 보여줄 데이터

		    if (!pageData || pageData.length === 0) {
		        $('#approvalTable').hide();
		        noDataMsg.show();
		        return;
		    }

		    $('#approvalTable').show();
		    noDataMsg.hide();

		    pageData.forEach(function(item, index) {
		        const status = item.approvalStatus;
		        const statusClass = status === '승인' ? 'color:blue;' : status === '반려' ? 'color:orange;' : '';
		        const buttonLabel = status === '승인' || status === '반려' ? '상세확인' : '확인 및 처리';

		        const row = '<tr>'
		            + '<td>' + (startIndex + index + 1) + '</td>'
		            + '<td>' + (item.requestDate || item.request_date) + '</td>'
		            + '<td>' + (item.requesterName || item.requester_name)
		                + ' (' + (item.requesterDept || item.requester_dept)
		                + ', ' + (item.requesterPosition || item.requester_position) + ')' + '</td>'
		            + '<td>' + (item.documentTitle || item.document_title) + '</td>'
		            + '<td>'
		            + '<button onclick="openPopup(\''
		                + item.approvalDocumentId + '\', \'' + item.approvalLineId
		                + '\', \'' + (item.approvalStatus || '') + '\')">' + buttonLabel + '</button>'
		            + '</td>'
		            + '<td style="' + statusClass + '">' + (status || '대기') + '</td>'
		            + '</tr>';

		        tbody.append(row);
		    });

		    renderPagination(pageNum); // 페이지네이션 버튼도 함께 그리기
		}
		
		function renderPagination(currentPage) {
		    const paginationContainer = $('#pagination');
		    paginationContainer.empty();

		    const totalItems = approvalData.length;
		    const totalPages = Math.ceil(totalItems / itemsPerPage);

		    if (totalPages <= 1) return;

		    const maxPagesToShow = 5;
		    const currentGroup = Math.ceil(currentPage / maxPagesToShow);

		    const startPage = (currentGroup - 1) * maxPagesToShow + 1;
		    let endPage = startPage + maxPagesToShow - 1;
		    if (endPage > totalPages) endPage = totalPages;

		    // 이전 그룹 버튼
		    if (startPage > 1) {
		        paginationContainer.append(`<button onclick="renderPage(${startPage - 1})">〈</button>`);
		    }

		    // 현재 그룹의 페이지 버튼들
		    for (let i = startPage; i <= endPage; i++) {
		        const btn = $('<button>')
		            .text(i)
		            .addClass(i === currentPage ? 'active' : '')
		            .click(() => renderPage(i));

		        paginationContainer.append(btn);
		    }

		    // 다음 그룹 버튼
		    if (endPage < totalPages) {
		        paginationContainer.append(`<button onclick="renderPage(${endPage + 1})">〉</button>`);
		    }
		}
		
		$(document).ready(function() {
			loadPendingList();
		});
	</script>
</div>
</div>
<jsp:include page="../common/footer.jsp" />