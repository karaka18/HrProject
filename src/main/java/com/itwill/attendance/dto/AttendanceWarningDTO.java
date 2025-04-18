package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 12.관리자용 근태 종합 조회 (지각/결근 기준 이상 시 경고)
 * 이 DTO는 관리자가 근태 현황을 종합적으로 조회할 때,
 * 지각과 결근 기준을 초과한 사용자에 대한 경고 정보를 포함합니다.
 */
@Data
@Builder
public class AttendanceWarningDTO {

    private String empId;       // 사원 ID
    private String empName;     // 사원 이름
    private String department;  // 부서명
    private int latenessCount;  // 지각 횟수
    private int absenceCount;   // 결근 횟수
    private String warningLevel; // 경고 수준 (예: '경고', '주의', '정상' 등)

}
