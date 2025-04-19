package com.itwill.attendance.service;

import java.time.LocalDate;
import java.util.List;

import com.itwill.attendance.dto.*;

public interface AttendanceService {

    // 사용자 출퇴근 기록 조회
    List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate);

    // 사용자 지각 조회
    List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate);

    // 사용자 근무 요약
    WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate);

    // 사용자 근태 상태 조회
    List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate);

    // 휴가 내역
    List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId);
    List<LeaveHistoryDTO> getMyLeaveHistoryByDate(String empId, String startDate, String endDate);

    // 휴가 잔여일수/신청
    LeaveBalanceDTO getMyLeaveBalance(String empId);
    void applyForLeave(LeaveDTO leaveDTO);

    // 관리자 조회/관리 기능
    List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate);
    void updateLeaveDays(LeaveUpdateRequestDTO dto);
    List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate);
    List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate);
    void updateAttendanceRecord(AttendanceUpdateDTO dto);
    List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate);

    // 출근/퇴근 처리
    void clockIn(String empId);
    void clockOut(String empId);

    // 기타 조회
    List<AttendanceDetailDTO> getAttendanceByEmpId(String empId);
    AttendanceDTO getTodayAttendance(String empId);
    AttendanceDetailDTO getAttendanceDetail(String empId, LocalDate date);
    List<AttendanceStatusDTO> getAttendanceStatusList();
}