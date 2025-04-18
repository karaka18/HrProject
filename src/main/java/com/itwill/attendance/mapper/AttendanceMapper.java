package com.itwill.attendance.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

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
import com.itwill.attendance.model.Attendance;

public interface AttendanceMapper {
    void insertAttendance(Attendance attendance);

	
	//사용자 출퇴근 기록 조회
    List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate);
    
    //사용자 지각 현황 조회
    List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate);
    
    //사용자 근무 요약 조회 
    WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate);
    
    //사용자 근태 항목 및 근무 형태 조회 
    List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate);
    
    //사용자 전체 휴가 내역 확인(모든기간)
    List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId);
    
    //사용자 특정 기간 내의 휴가 내역 확인
    List<LeaveHistoryDTO> getMyLeaveHistoryByDate(@Param("empId") String empId, @Param("startDate") String startDate, @Param("endDate") String endDate);
    
    //사용자 휴가 잔여 일수 확인 
    LeaveBalanceDTO getMyLeaveBalance(String empId);
    
    //사용자 휴가 신청 기능
    void applyForLeave(LeaveDTO leaveDTO);
    
    //관리자용 출퇴근 기록부 조회
    List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate);
    
    //관리자용 휴가 일수 수정
    void updateLeaveDays(LeaveUpdateRequestDTO dto);
    
    //관리자용 지각 현황 조회
    List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate);
    
    //관리자용 근무 형태 및 시간 조회 
    List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate);
    
    //관리자용 출퇴근 기록 수정
    void updateAttendanceRecord(AttendanceUpdateDTO dto);
    
    //관리자용 근태 종합 조회 
    List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate);
    
    // 출근 기록 추가
    void insertAttendance(AttendanceDTO attendanceDTO);

    // 퇴근 기록 수정
    void updateAttendance(AttendanceDTO attendanceDTO);
    
    void insertClockIn(@Param("empId") String empId);
    void updateClockOut(@Param("empId") String empId);
    


}