package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceStatusDTO {
	//사용자 근태 항목 조회 DTO

    private String empId;       // 사원 ID (DB)
    private String status;      // 근무 상태 (근무, 지각, 결근, 휴가 등)
    private String reason;      // 사유 (휴가 종류, 결근 사유 등)
    private String startDate;   // 시작일 (휴가/출장/결근 등)
    private String endDate;     // 종료일 (휴가/출장/결근 등)
}
