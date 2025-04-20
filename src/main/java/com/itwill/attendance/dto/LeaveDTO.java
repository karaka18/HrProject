package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class LeaveDTO {
    private String leaveId;    // 휴가 ID
    private String empId;      // 사원 ID
    private LocalDate startDate;  // 휴가 시작일
    private LocalDate endDate;    // 휴가 종료일
    private String leaveType; // 휴가 유형 (연차, 병가 등)
    private String reason;    // 사유
    private boolean isApproved; // 승인 여부
}
