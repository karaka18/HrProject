package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceSummaryDTO {
	//사용자 근무 조회 DTO

    private String empId;       // 사원 ID (DB)
    private String empName;
    private LocalDate workDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String isLate;
    private String isEarlyLeave;
    private Double workHours; 
}
