package com.itwill.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatenessAdminDTO {
	//관리자 지각 현황 조회 DTO
	
    private String empId;      // 사원 ID
    private String empName;    // 사원 이름 (JOIN: EMPLOYEE)
    private String departmentName; // 부서명 (JOIN: DEPARTMENT)
    private LocalDate lateDate; // 지각 날짜
    private String lateReason; // 지각 사유 (DB에 저장되지 않지만, 조회 시 필요)
    private LocalTime lateDuration; // 지각 시간 (지각이 발생한 시간)
    private String status;     // 관리자 확인 상태 (확인, 미확인)
    
    private LocalDateTime checkInTime;
    private String isLate;

}
