package com.itwill.approval.controller;

import com.itwill.approval.dto.*;
import com.itwill.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/apply")
    public String showApprovalForm() {
        return "approval/approval-apply";
    }

    @PostMapping("/apply")
    public String submitApprovalRequest(
            HttpServletRequest request,
            HttpSession session,
            @RequestParam("attachmentFiles") List<MultipartFile> files) {

        String empId = (String) session.getAttribute("id");
        if (empId == null) {
            return "redirect:/member/login"; // 로그인 체크 추가
        }

        ApprovalApplyDTO dto = new ApprovalApplyDTO();
        dto.setRequestComment(request.getParameter("comment"));
        String documentTitle = request.getParameter("documentTitle");
        String referenceTableName = request.getParameter("referenceTableName");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String typeCode = "";
        String approvalId = "";
        LeaveReqDTO leave = null;
        BusinessReqDTO business = null;

        if ("LEAVE".equals(referenceTableName)) {
            String leaveStatus = request.getParameter("leaveStatus");

            if ("반차".equals(leaveStatus)) typeCode = "01";
            else if ("연차".equals(leaveStatus)) typeCode = "02";
            else if ("병가".equals(leaveStatus)) typeCode = "03";
            else typeCode = "00";

            String prefix = empId + today + typeCode;
            int count = approvalService.countApprovalByPrefix(prefix);
            approvalId = prefix + String.format("%03d", count + 1);

            leave = new LeaveReqDTO();
            leave.setLeaveId(approvalId);
            leave.setEmpId(empId);
            leave.setLeaveStatus(leaveStatus);
            leave.setLeaveStartDate(LocalDate.parse(request.getParameter("leaveStartDate")));
            leave.setLeaveEndDate(LocalDate.parse(request.getParameter("leaveEndDate")));
            int days = (int) (leave.getLeaveEndDate().toEpochDay() - leave.getLeaveStartDate().toEpochDay()) + 1;
            leave.setLeaveDays(days);
        } else if ("BUSINESS".equals(referenceTableName)) {
            typeCode = "04";

            String prefix = empId + today + typeCode;
            int count = approvalService.countApprovalByPrefix(prefix);
            approvalId = prefix + String.format("%03d", count + 1);

            business = new BusinessReqDTO();
            business.setBusinessTripId(approvalId);
            business.setEmpId(empId);
            business.setTripLocation(request.getParameter("businessLocation"));
            business.setBusinessTripPurpose(request.getParameter("businessPurpose"));
            business.setBusinessTripStart(LocalDate.parse(request.getParameter("businessStartDate")));
            business.setBusinessTripEnd(LocalDate.parse(request.getParameter("businessEndDate")));
        }

        dto.setApprovalDocumentId(approvalId);
        dto.setDocumentTitle(documentTitle);
        dto.setReferenceTableName(referenceTableName);
        dto.setReferenceId(approvalId);
        dto.setRequester(empId);
        dto.setRegister(empId);
        dto.setAttachmentCount(files != null ? files.size() : 0);
        dto.setApproverList(request.getParameter("approverList"));

        approvalService.saveApprovalRequest(dto, files, leave, business);

        return "redirect:/approval/apply";
    }

    @GetMapping("/search-approvers")
    @ResponseBody
    public List<ApprovalSearchDTO> searchApprovers(@RequestParam("keyword") String keyword) {
        return approvalService.searchApprovers(keyword);
    }

    @PostMapping("/template/save")
    @ResponseBody
    public String saveApprovalTemplate(@RequestBody ApprovalTemplateRequestDTO requestDTO) {
        ApprovalLineTemplateDTO templateDTO = new ApprovalLineTemplateDTO();
        templateDTO.setTemplateId(requestDTO.getTemplateId());
        templateDTO.setTemplateName(requestDTO.getTemplateName());
        templateDTO.setOwnerId(requestDTO.getOwnerId());

        approvalService.saveApprovalLineTemplate(templateDTO, requestDTO.getDetailList());
        return "success";
    }

    @GetMapping("/template/list-full")
    @ResponseBody
    public List<ApprovalLineTemplateListDTO> getTemplatesWithDetails(@RequestParam("ownerId") String ownerId) {
        return approvalService.getTemplatesWithDetails(ownerId);
    }

    @GetMapping("/template/check-name")
    @ResponseBody
    public boolean checkTemplateName(@RequestParam("name") String name) {
        return approvalService.isTemplateNameDuplicate(name);
    }

    @GetMapping("/template/list")
    @ResponseBody
    public List<ApprovalLineTemplateListDTO> getTemplatesByOwner(@RequestParam("ownerId") String ownerId) {
        return approvalService.getTemplatesByOwner(ownerId);
    }

    @GetMapping("/template/detail")
    @ResponseBody
    public List<ApprovalLineDetailDTO> getTemplateDetails(@RequestParam("templateId") String templateId) {
        return approvalService.getTemplateDetails(templateId);
    }

    @GetMapping("/my-documents")
    @ResponseBody
    public List<PendingApprovalDTO> getMyDocuments(HttpSession session) {
        String empId = (String) session.getAttribute("id");
        if (empId == null) return Collections.emptyList();
        return approvalService.getMyRequestedDocuments(empId);
    }
}