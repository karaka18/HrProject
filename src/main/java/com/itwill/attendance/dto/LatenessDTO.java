package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [2.사용자 지각 현황 조회 DTO]
 */

@Data
@Builder
public class LatenessDTO {
    private String attendanceId;       // 근태 기록 ID
    private String empId;              // 사원 ID
    private String empName;            // 사원 이름
    private String departmentName;     // 부서 이름
    private String date;               // 날짜
    private String arrivalTime;        // 출근 시간
//    private String reason;             // 지각 사유
//    private String approvalStatus;     // 승인 상태 (대기/승인/반려)
//    private int lateMinutes;           // 지각 시간(분)
    
    
    private String configuredStartTime; // 출근 시간 설정
    private int latenessCount;          // 지각 횟수
    private String latenessDates;       // 지각 날짜들 (예: "2025-04-01, 2025-04-02")
}


