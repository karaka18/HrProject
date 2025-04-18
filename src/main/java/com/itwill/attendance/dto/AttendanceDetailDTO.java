package com.itwill.attendance.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDetailDTO {

    // 기본 근태 정보
    private String attendanceId;
    private String empId;
    private String departmentId;
    private String departmentName;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String attendanceStatus;
    private String empName;      
    private String depName; 

    // 근무 일자 및 시간
    private String workDate;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

    // 근무 상태
//    private String isLate;
//    private String isAbsent;
//    private String isBusinessTrip; 주석처리된 것들은 현재 내 DB에 없어서 애플리케이션 로직에서 처리하는 것들
    private String isEarlyLeave;
//    private String workForm;

    // 지각 사유 및 상태
//    private String latenessReason;
//    private String reasonStatus;

    // 휴가 정보
//    private String isLeave;
    private String leaveType;
//    private String leaveStatus;

    // 근무 시간
//    private Integer totalWorkMinutes;

    // 생성/수정 시간
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
