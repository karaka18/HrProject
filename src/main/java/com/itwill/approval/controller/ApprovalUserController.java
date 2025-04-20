package com.itwill.approval.controller;

import com.itwill.approval.dto.ApprovalDetailDTO;
import com.itwill.approval.dto.ApprovalSearchDTO;
import com.itwill.approval.dto.PendingApprovalDTO;
import com.itwill.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalUserController {

    private final ApprovalService approvalService;

    // ✅ 사용자 결재 이력 화면 (기존)
    @GetMapping("/user")
    public String showUserApprovalPage() {
        return "approval/approval-user"; // 사용자용 사이드바 포함
    }

    // ✅ 관리자용 결재 이력 화면 (복사본)
    @GetMapping("/user/a")
    public String showUserApprovalPageCopy() {
        return "approval/approval-user-a"; // 관리자용 사이드바 포함
    }

    // ✅ 공통 AJAX: 결재 신청 목록
    @GetMapping("/user-documents")
    @ResponseBody
    public List<PendingApprovalDTO> getMyDocuments(HttpSession session) {
        String empId = (String) session.getAttribute("id");
        if (empId == null) return Collections.emptyList();

        return approvalService.getMyRequestedDocuments(empId);
    }

    // ✅ 공통 AJAX: 결재 상세 정보
    @GetMapping("/user-detail")
    @ResponseBody
    public ApprovalDetailDTO getApprovalDetailJson(@RequestParam("documentId") String docId) {
        return approvalService.getApprovalDetail(docId);
    }
}