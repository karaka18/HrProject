package com.itwill.attendance.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkInputDTO {
    private String empId;         // 사원 ID
    private LocalDateTime checkInTime;  // 출근 시간
    private LocalDateTime checkOutTime; // 퇴근 시간
    private String workType;      // 근무 형태 (정상근무, 지각, 결근, 휴가 등)
    private String absenceReason; // 결근 사유 (결근 시 필요)
}
