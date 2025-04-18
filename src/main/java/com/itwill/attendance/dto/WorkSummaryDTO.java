package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [3. 사용자 근무 요약 조회 DTO]
 * - 기간별 누적 근무 일수, 총 근무 시간, 총 지각 횟수, 결근 횟수 등을 요약하여 전달
 */
@Data
@Builder
public class WorkSummaryDTO {

    private String empId;            // 사원 ID
    private String empName;          // 사원 이름
    private String departmentName;   // 부서명

    private int totalWorkDays;       // 총 근무 일수
    private int totalWorkHours;      // 총 근무 시간 (시간 단위)
    private int totalLateCount;      // 총 지각 횟수
    private int totalAbsentCount;    // 총 결근 횟수
    private int totalBusinessTripCount; // 총 외근 횟수
}
