package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDetailDTO {
	//사용자 상세 근무 정보 DTO
	//관리자 출퇴근 기록 상세 조회 DTO

    private String attendanceId;       // 근태 ID (DB에 있음)
    private String empId;              // 사원 ID (DB에 있음)
    private LocalDate workDate;        // 근무 일자 (DB: DATE 형)
    private LocalDateTime checkInTime; // 출근 시간 (DB: check_in_time)
    private LocalDateTime checkOutTime;// 퇴근 시간 (DB: check_out_time)
    private int workDays;              // 근무 일수 (DB: 있음)
    private double workHours;          // 총 근무 시간 (DB: 있음)
    private double nightWorkHour;      // 야간 근무 시간 (DB: 있음)

    private String empName;            // 사원명 (JOIN: employee) ⚠️ DB에 없음
    private String departmentName;     // 부서명 (JOIN: department) ⚠️ DB에 없음
    private String isLate;             // 지각 여부 (앱에서 계산) ⚠️ DB에 없음

    private long workMinutes;

    
    public long getWorkMinutes() {
        if (checkInTime != null && checkOutTime != null) {
            return java.time.Duration.between(checkInTime, checkOutTime).toMinutes();
        }
        return 0;
    }



}
