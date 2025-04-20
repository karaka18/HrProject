package com.itwill.attendance.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwill.attendance.dto.*;

public interface AttendanceMapper {
	
	// 사용자 출퇴근 기록 (일자별 1건), (+조회기능 추가하기)
    AttendanceDetailDTO selectAttendanceDetail(@Param("empId") String empId, @Param("date") LocalDate date);
	
    //사용자 지각 현황
    List<LateAttendanceDTO> selectLateAttendancesByEmpId(String empId);

    //사용자 근무 조회
    List<WorkRecordDTO> selectWorkRecordsByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    //사용자 근태 항목
    List<AttendanceStatusDTO> selectAttendanceStatusByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    
    //사용자 휴가 내역 확인 및 신청
    List<LeaveDTO> selectLeaveHistoryByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

int selectRemainingLeaveDays(@Param("empId") String empId);

    
//관리자 출퇴근 기록부 및 현황 조회
List<AttendanceDetailDTO> selectAttendanceDetails(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

AttendanceDetailDTO selectAttendanceDetailByEmpIdAndDate(@Param("empId") String empId,
                @Param("date") LocalDate date);


//관리자 휴가 일수 조회
//사원별 휴가 상태 조회
LeaveStatusDTO selectLeaveStatusByEmpId(String empId);

//전체 사원의 휴가 상태 조회
List<LeaveStatusDTO> selectAllLeaveStatuses();





    
	 AttendanceDTO selectTodayAttendance(@Param("empId") String empId, @Param("date") LocalDate date);

	    void insertCheckIn(AttendanceDTO dto);

	    void updateCheckOut(@Param("empId") String empId, @Param("date") LocalDate date, @Param("checkOutTime") LocalTime time);

	    AttendanceDetailDTO selectAttendanceDetail(@Param("empId") String empId, @Param("date") LocalDate date);

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
    void applyForLeave(LeaveDTO leaveDTO);
    void updateLeaveDays(LeaveUpdateRequestDTO dto);
}