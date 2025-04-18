package com.itwill.attendance.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDetailDTO {

    // 기본 근태 정보
    private String attendanceId;
    private String empId;
    private String empName;
    private String departmentId;
    private String departmentName;

    // 근무 일자 및 시간
    private String workDate;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

    // 근무 상태
    private String isLate;
    private String isAbsent;
    private String isBusinessTrip;
    private String isEarlyLeave;
    private String workForm;

    // 지각 사유 및 상태
    private String latenessReason;
    private String reasonStatus;

    // 휴가 정보
    private String isLeave;
    private String leaveType;
    private String leaveStatus;

    // 근무 시간
    private Integer totalWorkMinutes;

    // 생성/수정 시간
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
