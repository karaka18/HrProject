package com.itwill.attendance.dto;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDTO {

    private String attendanceId;       // 근태 ID
    private String empId;              // 사원 ID
    private LocalDate workDate;        // 근무 일자 (LocalDate)
    private LocalDateTime checkInTime; // 출근 시간 (LocalDateTime)
    private LocalDateTime checkOutTime;// 퇴근 시간 (LocalDateTime)
    private int workDays;              // 근무 일수
    private double workHours;          // 총 근무 시간
    private double nightWorkHour;      // 야간 근무 시간
    private String empName;            // 사원명
    private String departmentName;     // 부서명
    private String isLate;             // 지각 여부

    private long workMinutes;

    public long getWorkMinutes() {
        if (checkInTime != null && checkOutTime != null) {
            return java.time.Duration.between(checkInTime, checkOutTime).toMinutes();
        }
        return 0;
    }

    // AttendanceDTO를 AttendanceDetailDTO로 변환하는 메서드
    public AttendanceDetailDTO toAttendanceDetailDTO() {
        AttendanceDetailDTO detailDTO = new AttendanceDetailDTO();
        detailDTO.setAttendanceId(this.attendanceId);
        detailDTO.setEmpId(this.empId);
        detailDTO.setDate(Date.valueOf(this.workDate)); // LocalDate -> Date
        detailDTO.setWorkDate(Timestamp.valueOf(this.checkInTime)); // LocalDateTime -> Timestamp
        detailDTO.setWorkDays(this.workDays);
        detailDTO.setWorkHours(this.workHours);
        detailDTO.setCheckInTime(Time.valueOf(this.checkInTime.toLocalTime())); // LocalDateTime -> Time
        detailDTO.setCheckOutTime(Time.valueOf(this.checkOutTime.toLocalTime())); // LocalDateTime -> Time
        detailDTO.setNightWorkHour(this.nightWorkHour);
        detailDTO.setCreatedAt(Timestamp.valueOf(this.checkInTime)); // 예시: 생성시간을 출근 시간으로 설정
        detailDTO.setUpdatedAt(Timestamp.valueOf(this.checkOutTime)); // 예시: 수정시간을 퇴근 시간으로 설정
        detailDTO.setEmpName(this.empName);
        detailDTO.setDepartmentName(this.departmentName);
        return detailDTO;
    }
}
