package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveDTO {
    private String leaveId;         // 휴가 ID
    private String empId;           // 사원 ID
    private String requestDate;     // 신청일
    private String startDate;       // 휴가 시작일
    private String endDate;         // 휴가 종료일
//    private String reportFile;      // 휴가 신청 관련 보고서 파일명
//    private String approvalStatus;  // 승인 상태 (승인, 대기, 반려 등)
    private String empName;        // 사원 이름 → EMPLOYEE.emp_name (조인)
    private String departmentName; // 부서 이름 → DEPARTMENT.dep_name (조인)
    private String approverName;   // 결재자 이름 → SIGNATURES.signer_id → EMPLOYEE 조인
    private String approvalDate;   // 결재 완료일 → APPROVAL.approval_date

}
