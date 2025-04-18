package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [5. 사용자 휴가 내역 조회 DTO]
 * - 사용자가 신청한 휴가 기록 및 승인 현황을 담는 DTO
 * - 사용자가 본인의 휴가 내역을 조회할 때 사용
 */
@Data
@Builder
public class LeaveHistoryDTO {
    private String empId;            // 사원 ID
    private String empName;          // 사원 이름
    private String leaveType;        // 휴가 유형 (연차, 병가 등)
    private String startDate;        // 휴가 시작일
    private String endDate;          // 휴가 종료일
    private String leaveReason;      // 휴가 사유
    private String approvalStatus;   // 휴가 승인 상태 (승인, 대기, 반려 등)
}
