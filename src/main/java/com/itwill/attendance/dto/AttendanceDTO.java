package com.itwill.attendance.dto;

import lombok.Data;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
public class AttendanceDTO {

	private String attendanceId; // 근태 기록 고유 ID (날짜+사번+형태 등으로 구성)
    private String empId; // 사원 ID
    private Date date; // 근무 일자 (YYYY-MM-DD)
    private Timestamp workDate; // 근무 등록 시간 (DB 자동 생성)
    private Integer workDays; // 근무 일수 (출근한 날짜 수 기반 자동 계산)
    private Double workHours; // 총 근무 시간 (출퇴근 시간 차이로 자동 계산)
    private Time checkInTime; // 출근 시간
    private Time checkOutTime; // 퇴근 시간
    private Double nightWorkHour; // 야간 근무 시간 (지정 시간 이후 자동 계산)
    private Double holidayWorkHour; // 휴일 근무 시간 (휴일 근무시 자동 계산)
    private Timestamp createdAt; // 기록 생성 시간 (DB 자동)
    private Timestamp updatedAt; // 기록 수정 시간 (DB 자동)

    // 조회를 위한 추가 필드들 (JOIN 결과로 가져옴)
    private String empName; // 사원 이름 (JOIN: EMPLOYEE)
    private String departmentName; // 부서명 (JOIN: DEPARTMENT)

    // 지각 관련 필드 (애플리케이션에서 자동 계산)
    private Integer lateDays; // 지각 일수
    private String lateDates; // 지각 날짜 리스트 (예: "2025-04-03, 2025-04-07")

    // 근무 형태 구분 (출근, 지각, 결근, 휴가, 출장 등)
    private String workType; // 근무 형태

    // 근무 기간 (휴가/출장일 경우에만 표시)
    private String period; // 휴가/출장 기간

    // 추가적인 상태값 (예: 결근, 지각 등)
    private String absenceType; // 결근 유형
    private String absenceReason; // 결근 사유
}
