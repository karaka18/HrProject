package com.itwill.attendance.dto;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatenessDTO {

    private String attendanceId;       // 근태 ID (DB에 있음)
    private String empId;              // 사원 ID (DB에 있음)
    private String empName;            // 사원 이름 (JOIN: employee) ⚠️ DB에 없음
    private String departmentName;     // 부서명 (JOIN: department) ⚠️ DB에 없음
    private LocalDate workDate;        // 근무 일자 (DB에 있음)
    private LocalDateTime checkInTime; // 출근 시간 (DB: check_in_time)
    private String isLate;             // 지각 여부 (앱에서 계산) ⚠️ DB에 없음

    private LocalDate lateDate;  // 지각 날짜
    private LocalTime lateDuration; // 지각 시간
    private String lateReason;   // 지각 사유 (사유서에서 가져옴)
    private String status;       // 확인 상태 (확인, 미확인)
    
}
