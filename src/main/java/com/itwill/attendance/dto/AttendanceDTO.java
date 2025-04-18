package com.itwill.attendance.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDTO {

    // 고유 출퇴근 기록 ID (예: 250302250010001att)
    private String attendanceId;

    private String status; // 출근/퇴근 상태 (DB에 없는데..?)
    
    // 사원 번호
    private String empId;

    // 근무 일자 (yyyy-MM-dd)
    private String workDate;

    // 출근 시간
    private LocalDateTime clockIn;

    // 퇴근 시간
    private LocalDateTime clockOut;

    // 지각 여부 (Y/N)
    private String isLate;

    // 결근 여부 (Y/N)
    private String isAbsent;

    // 외근 여부 (Y/N)
    private String isBusinessTrip;

    // 조퇴 여부 (Y/N)
    private String isEarlyLeave;

    // 지각 사유 (입력 optional)
    private String latenessReason;

    // 지각 사유 처리 상태 (WAITING, APPROVED, REJECTED 등)
    private String reasonStatus;

    // 총 근무 시간 (분 단위로 계산, DB에서 계산해서 넣을 수도 있음)
    private Integer totalWorkMinutes;

    // 휴가 여부 (Y/N)
    private String isLeave;

    // 휴가 타입 (연차, 병가, 특별휴가 등)
    private String leaveType;

    // 휴가 승인 상태 (WAITING, APPROVED, REJECTED 등)
    private String leaveStatus;

    // 근무 형태 (예: 정상근무, 재택근무, 외근 등)
    private String workForm;

    // 기록 생성 시간
    private LocalDateTime createdTime;

    // 기록 수정 시간
    private LocalDateTime updatedTime;
    
    private LocalDateTime arrivalTime;
    private LocalDateTime leavetime;
    private String remark;
    
}
