package com.itwill.approval.controller;

import com.itwill.approval.dto.*;
import com.itwill.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalAdminController {
	private final ServletContext servletContext;
	private final ApprovalService approvalService;

	// ✅ 원래 관리자 페이지
	@GetMapping("/admin")
	public String showAdminPage() {
		return "approval/approval-admin-inbox"; // 기본 admin-sidebar.jsp 포함된 화면
	}

	// ✅ 복사된 관리자 페이지 (다른 사이드바 포함된 JSP)
	@GetMapping("/admin/a")
	public String showAdminPageCopy() {
		return "approval/approval-admin-inbox-a"; // 복사된 JSP
	}

	// ================================
	// AJAX: 결재 대기 목록
	// ================================
	@GetMapping("/inbox")
	@ResponseBody
	public List<PendingApprovalDTO> getPendingApprovals(@RequestParam("empId") String empId) {
	    return approvalService.getPendingApprovals(empId);
	}

	// ================================
	// AJAX: 상세 팝업 HTML
	// ================================
	@GetMapping(value = "/detail", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getApprovalDetail(@RequestParam("documentId") String documentId) {
	    ApprovalDetailDTO detail = approvalService.getApprovalDetail(documentId);

	    StringBuilder html = new StringBuilder();
	    html.append("<h3>").append(detail.getDocumentTitle() != null ? detail.getDocumentTitle() : "-").append("</h3>");

	    if ("LEAVE".equals(detail.getReferenceTableName())) {
	        html.append("<p><strong>휴가 구분:</strong> ")
	            .append(detail.getLeaveStatus() != null ? detail.getLeaveStatus() : "-")
	            .append("</p>")
	            .append("<p><strong>휴가 기간:</strong> ")
	            .append(detail.getLeaveStartDate() != null ? detail.getLeaveStartDate() : "-")
	            .append(" ~ ")
	            .append(detail.getLeaveEndDate() != null ? detail.getLeaveEndDate() : "-")
	            .append(" (").append(detail.getLeaveDays()).append("일)</p>");
	    } else if ("BUSINESS".equals(detail.getReferenceTableName())) {
	        html.append("<p><strong>출장지:</strong> ")
	            .append(detail.getTripLocation() != null ? detail.getTripLocation() : "-")
	            .append("</p>")
	            .append("<p><strong>출장 목적:</strong> ")
	            .append(detail.getBusinessTripPurpose() != null ? detail.getBusinessTripPurpose() : "-")
	            .append("</p>")
	            .append("<p><strong>출장 기간:</strong> ")
	            .append(detail.getBusinessTripStart() != null ? detail.getBusinessTripStart() : "-")
	            .append(" ~ ")
	            .append(detail.getBusinessTripEnd() != null ? detail.getBusinessTripEnd() : "-")
	            .append(" (").append(detail.getTripDays()).append("일)</p>");
	    }

	    html.append("<p><strong>코멘트:</strong> ")
	        .append(detail.getComment() != null ? detail.getComment() : "-")
	        .append("</p>");

	    if (detail.getFiles() != null && !detail.getFiles().isEmpty()) {
	        html.append("<p><strong>첨부파일:</strong></p><ul>");
	        for (FileDTO file : detail.getFiles()) {
	            if (file != null) {
	                html.append("<li><a href='/approval/download?fileId=")
	                    .append(file.getFileId()).append("'>")
	                    .append(file.getFileName() != null ? file.getFileName() : "(이름 없음)")
	                    .append("</a></li>");
	            }
	        }
	        html.append("</ul>");
	    }

	    if (detail.getApprovers() != null && !detail.getApprovers().isEmpty()) {
	        html.append("<p><strong>결재선:</strong><br>");
	        for (int i = 0; i < detail.getApprovers().size(); i++) {
	            ApprovalLineWithStatusDTO approver = detail.getApprovers().get(i);
	            String status = approver.getStatus() != null ? approver.getStatus() : "대기";
	            String statusClass = "";

	            switch (status) {
	                case "승인": statusClass = "approved"; break;
	                case "반려": statusClass = "rejected"; break;
	                case "취소": statusClass = "cancelled"; break;
	                default: statusClass = "pending"; break;
	            }

	            html.append((i + 1)).append(". ")
	                .append(approver.getName()).append(" (")
	                .append(approver.getDept()).append(" / ")
	                .append(approver.getPosition()).append(") ")
	                .append("<span class='status ").append(statusClass).append("'>(")
	                .append(status).append(")</span>");

	            if (i < detail.getApprovers().size() - 1) html.append(" → ");
	        }
	        html.append("</p>");
	    }

	    return html.toString();
	}

	// ================================
	// 첨부파일 다운로드
	// ================================
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileId") String fileId) {
	    FileDTO fileDTO = approvalService.getFileById(fileId);
	    if (fileDTO == null || fileDTO.getFilePath() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    String uploadFolderPath = servletContext.getRealPath("/resources/upload/approval");
	    String fileNameOnly = fileDTO.getFilePath().substring(fileDTO.getFilePath().lastIndexOf("/") + 1);
	    String realPath = uploadFolderPath + File.separator + fileNameOnly;

	    File file = new File(realPath);
	    if (!file.exists()) {
	        return ResponseEntity.notFound().build();
	    }

	    Resource resource = new FileSystemResource(file);
	    String encodedFileName = URLEncoder.encode(fileDTO.getFileName(), StandardCharsets.UTF_8).replace("+", "%20");

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
	        .header(HttpHeaders.CONTENT_TYPE, fileDTO.getFileType())
	        .body(resource);
	}

	// ================================
	// 결재 승인 처리
	// ================================
	@PostMapping("/approve")
	@ResponseBody
	public void approve(@RequestBody ApprovalAppDTO dto, HttpSession session) {
	    dto.setApprovalStatus("승인");
	    String empId = (String) session.getAttribute("id");

	    if (empId != null) {
	        dto.setRegister(empId);
	        dto.setModifier(empId);
	    } else {
	        throw new RuntimeException("로그인 정보 없음");
	    }

	    approvalService.saveApprovalResult(dto);
	    approvalService.activateNextApprover(dto.getApprovalDocumentId(), dto.getApprovalOrder() + 1);
	}

	// ================================
	// 결재 반려 처리
	// ================================
	@PostMapping("/reject")
	@ResponseBody
	public void reject(@RequestBody ApprovalAppDTO dto, HttpSession session) {
	    dto.setApprovalStatus("반려");
	    String empId = (String) session.getAttribute("id");

	    if (empId != null) {
	        dto.setRegister(empId);
	        dto.setModifier(empId);
	    } else {
	        throw new RuntimeException("로그인 정보 없음");
	    }

	    approvalService.saveApprovalResult(dto);
	}
}