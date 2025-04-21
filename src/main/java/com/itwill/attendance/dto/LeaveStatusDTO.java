package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LeaveStatusDTO {
	//관리자 휴가 상태 조회 DTO
	
    private String empId;        // 사원 ID
    private String empName;      // 사원 이름
    private String departmentName; // 부서명
    private int totalLeaveDays;  // 총 휴가 일수
    private int usedLeaveDays;   // 사용한 휴가 일수
    private int remainingLeaveDays; // 남은 휴가 일수
}
