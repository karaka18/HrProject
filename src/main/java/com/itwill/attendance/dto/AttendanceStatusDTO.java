package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [4. 사용자 근태 항목 및 근무 형태 조회 DTO]
 * - 일자별 근무 상태(정상근무, 지각, 결근, 외근 등)를 담는 DTO
 * - 사용자용/관리자용 통합 조회에 사용
 */
@Data
@Builder
public class AttendanceStatusDTO {

    private String empId;            // 사원 ID
    private String empName;          // 사원 이름
    private String departmentName;   // 부서명

    private String date;             // 해당 일자
    private String workType;         // 근무 유형 (정상근무, 지각, 결근, 외근 등)
    private String checkInTime;      // 출근 시간
    private String checkOutTime;     // 퇴근 시간
    private String remarks;          // 비고 또는 사유
}
