package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkSummaryDTO {

    private LocalDate workDate;           // 근무일자 (DB)
    private LocalDateTime checkInTime;    // 출근시간 (DB)
    private LocalDateTime checkOutTime;   // 퇴근시간 (DB)
    private long workMinutes;             // 근무 시간(분) ⚠️ DB에 없음
    private String empId;                 // 사원 ID (DB)
    private String empName;               // 사원 이름 ⚠️ JOIN (DB 없음)
}
