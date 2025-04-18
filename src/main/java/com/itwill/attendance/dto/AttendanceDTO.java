package com.itwill.attendance.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Data;

//근태 관련 기본 DTO

@Data
public class AttendanceDTO {
    private String attendanceId;
    private String empId;
    private LocalDateTime workStartTime;  // 출근 시간
    private LocalDateTime workEndTime;    // 퇴근 시간
    private int workHours;                // 근무 시간 (계산된 값)
    private int workDays;                 // 근무 일수 (계산된 값)
    private String configuredStartTime;  // 출근 시간 설정
    private int latenessCount;           // 지각 횟수
    private String latenessDates;        // 지각 날짜들
    
}
