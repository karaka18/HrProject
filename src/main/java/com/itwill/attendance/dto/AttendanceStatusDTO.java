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

//    private String date;             // 해당 일자
//    private String workType;         // 근무 유형 (정상근무, 지각, 결근, 외근 등)
    private String checkInTime;      // 출근 시간
    private String checkOutTime;     // 퇴근 시간
//    private String remarks;          // 비고 또는 사유
    
    private String approvalStatus;   // 승인 상태 (승인, 반려, 대기 등)

 // 아래 필드는 실제 DB에는 존재하지 않으나, 애플리케이션 로직(Mapper 등)에서 생성 가능하다면 사용
    private String attendanceStatusId; // 근태 상태 통합 ID (예: "20250418EMP001ATT" 등)

    public String determineAttendanceStatus(LocalDate today, AttendanceDTO attendanceDTO) {
        // 오늘 날짜에 대한 출퇴근 정보 처리
        if (attendanceDTO.getWorkStartTime() != null && attendanceDTO.getWorkEndTime() == null) {
            return "출근";
        } else if (attendanceDTO.getWorkStartTime() != null && attendanceDTO.getWorkEndTime() != null) {
            return "퇴근";
        }
        // 출장 처리 (예시)
        if (attendanceDTO.getWorkStartTime() != null && /* 출장 관련 조건 */) {
            return "출장";
        }
        // 휴가 처리 (예시)
        if (attendanceDTO.getWorkStartTime() != null && /* 휴가 관련 조건 */) {
            return "휴가";
        }
        return "결근"; // 기본 상태
    }
 
    
}
