package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [11. 관리자용 근무 입력/수정/삭제 기능 DTO]
 * - 관리자가 사원의 출퇴근 기록을 입력, 수정, 삭제할 때 사용하는 DTO
 * - 출퇴근 일시 및 근무 상태를 포함
 */
@Data
@Builder
public class AttendanceUpdateDTO {

    private String attendanceId;      // 출퇴근 기록 ID
    private String empId;             // 사원 ID
    private String attendanceDate;    // 출퇴근 일자
    private String checkInTime;       // 출근 시간
    private String checkOutTime;      // 퇴근 시간
    private String attendanceStatus;  // 근태 상태 (출근, 퇴근, 결근 등)
}
