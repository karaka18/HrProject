package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.Duration; 

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDTO {

	private String attendanceId;        // 근태 ID
    private String empId;               // 사원 ID
    private LocalDate workDate;         // 근무 일자
    private LocalDateTime checkInTime;  // 출근 시간
    private LocalDateTime checkOutTime; // 퇴근 시간

    private int workDays;               // 근무 일수
    private double workHours;           // 총 근무 시간
    private double nightWorkHour;       // 야간 근무 시간
    private long workMinutes;           // 총 근무 시간 (분 단위)

    private String empName;             // 사원명
    private String departmentName;      // 부서명
    private String isLate;              // 지각 여부 (Y/N)

    private String lateReason;          // 지각 사유
    private String absenceReason;       // 결근 사유


    // 서버 내부에서 AttendanceDetailDTO로 변환할 때 사용
    public AttendanceDetailDTO toAttendanceDetailDTO() {
        return AttendanceDetailDTO.builder()
            .attendanceId(this.attendanceId)
            .empId(this.empId)
            .date(java.sql.Date.valueOf(this.workDate))
            .workDate(java.sql.Timestamp.valueOf(this.checkInTime))
            .checkInTime(java.sql.Time.valueOf(this.checkInTime.toLocalTime()))
            .checkOutTime(java.sql.Time.valueOf(this.checkOutTime.toLocalTime()))
            .workDays(this.workDays)
            .workHours(this.workHours)
            .nightWorkHour(this.nightWorkHour)
            .workMinutes(this.getWorkMinutes())
            .empName(this.empName)
            .departmentName(this.departmentName)
            .lateReason(this.lateReason)
            .absenceReason(this.absenceReason)
            .createdAt(java.sql.Timestamp.valueOf(this.checkInTime))
            .updatedAt(java.sql.Timestamp.valueOf(this.checkOutTime))
            .build();
    }

    // 분 단위 근무 시간 자동 계산
    public long getWorkMinutes() {
        if (checkInTime != null && checkOutTime != null) {
            return Duration.between(checkInTime, checkOutTime).toMinutes(); // Duration.between() 사용
        }
        return 0;
    }
}
