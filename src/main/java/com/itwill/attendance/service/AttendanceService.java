package com.itwill.attendance.service;

import java.util.List;

import com.itwill.attendance.dto.AttendanceDTO;
import com.itwill.attendance.dto.AttendanceDetailDTO;
import com.itwill.attendance.dto.AttendanceStatusDTO;
import com.itwill.attendance.dto.AttendanceUpdateDTO;
import com.itwill.attendance.dto.AttendanceWarningDTO;
import com.itwill.attendance.dto.LatenessDTO;
import com.itwill.attendance.dto.LeaveBalanceDTO;
import com.itwill.attendance.dto.LeaveDTO;
import com.itwill.attendance.dto.LeaveHistoryDTO;
import com.itwill.attendance.dto.LeaveUpdateRequestDTO;
import com.itwill.attendance.dto.WorkSummaryDTO;

public interface AttendanceService {

    // [1] 사용자 출퇴근 기록 조회 + 현황
    List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate);

    // [2] 사용자 지각 현황 조회
    List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate);

    // [3] 사용자 근무 요약 조회
    WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate);

    // [4] 사용자 근태 항목 및 근무 형태 조회
    List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate);

    // [5] 사용자 전체 휴가 내역 확인(모든 기간)
    List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId);

    // [5-1] 사용자 특정 기간 내 휴가 신청 내역 + 보고서 다운로드 등
    List<LeaveHistoryDTO> getMyLeaveHistoryByDate(String empId, String startDate, String endDate);
    
    // [6] 사용자 휴가 잔여 일수 확인 (단순 수치만 반환)
    LeaveBalanceDTO getMyLeaveBalance(String empId);
    
    // [6-1] 사용자 휴가 신청
    void applyForLeave(LeaveDTO leaveDTO);

    // [7] 관리자용 출퇴근 기록부 조회 (사원별/부서별/일자별)
    List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate);

    // [8] 관리자용 사원별 휴가 일수 수정
    void updateLeaveDays(LeaveUpdateRequestDTO dto);

    // [9] 관리자용 지각 현황 조회 및 경고 관리
    List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate);

    // [10] 관리자용 근무 형태 및 시간 조회
    List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate);

    // [11] 관리자용 근무 정보 입력/수정/삭제
    void updateAttendanceRecord(AttendanceUpdateDTO dto);

    // [12] 관리자용 근태 종합 경고/요약 현황 조회
    List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate);

    // [13] 사용자 출근 기록 등록
    void registerAttendance(AttendanceDTO attendanceDTO);

    // [14] 사용자 출근 처리
    void clockIn(String empId);  

    // [15] 사용자 퇴근 처리
    void clockOut(String empId); 
    
    
}
