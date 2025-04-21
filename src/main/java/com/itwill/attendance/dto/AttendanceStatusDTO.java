package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceStatusDTO {
    private String empId;
    private String empName;
    private LocalDate workDate;
    private String status;          // 근무 상태: 출근, 결근, 지각 등
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Boolean isLate;
    private Boolean isEarlyLeave;
    private Double workHours;
}

