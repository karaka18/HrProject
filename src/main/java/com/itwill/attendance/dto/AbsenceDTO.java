package com.itwill.attendance.dto;


import java.time.LocalDate;

import com.itwill.attendance.LeaveStatus.AbsenceType;

import lombok.Data;



@Data
public class AbsenceDTO {
	
	// AbsenceDTO.java

	
	    private String absenceId;      // 결근 ID
	    private String empId;          // 사원 ID
	    private LocalDate absenceDate; // 결근 날짜
	    private AbsenceType absenceType; // 결근 유형 (enum 사용)
	    private String absenceReason;  // 결근 사유
	    private LocalDate createdAt;   // 기록 생성 시간
	    private LocalDate updatedAt;   // 기록 수정 시간
	


}
