package com.itwill.attendance.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwill.attendance.dto.*;

public interface AttendanceMapper {

    // 출근/퇴근 처리
    void insertAttendance(AttendanceDTO dto);
    void updateClockOut(@Param("empId") String empId, @Param("now") LocalDateTime now);
    boolean hasCheckedInToday(@Param("empId") String empId);

    // 오늘 출근 정보
    AttendanceDTO selectTodayAttendance(@Param("empId") String empId);

    // 날짜별 상세 조회
    AttendanceDetailDTO getAttendanceDetail(@Param("empId") String empId, @Param("date") LocalDate date);

    // 사용자 조회용
    List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate);
    List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate);
    WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate);
    List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate);

    // 관리자용
    List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate);
    void updateAttendanceRecord(AttendanceUpdateDTO dto);
    List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate);
    List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate);
    List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate);

    // 기타
    List<AttendanceDetailDTO> selectAttendanceByEmpId(@Param("empId") String empId);
    List<AttendanceStatusDTO> selectAttendanceStatusList();

    // 휴가
    List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId);
    List<LeaveHistoryDTO> getMyLeaveHistoryByDate(@Param("empId") String empId, @Param("startDate") String startDate, @Param("endDate") String endDate);
    LeaveBalanceDTO getMyLeaveBalance(String empId);
    void applyForLeave(LeaveDTO leaveDTO);
    void updateLeaveDays(LeaveUpdateRequestDTO dto);
}