package com.itwill.attendance.service;

import java.time.LocalDate;
import java.util.List;

import com.itwill.attendance.dto.AttendanceDetailDTO;
import com.itwill.attendance.dto.AttendanceStatusDTO;
import com.itwill.attendance.dto.LatenessDTO;
import com.itwill.attendance.dto.LeaveDTO;
import com.itwill.attendance.dto.LeaveStatusDTO;
import com.itwill.attendance.dto.WorkInputDTO;
import com.itwill.attendance.dto.WorkTypeAdminDTO;

public interface AttendanceService{
	
	//사용자 출퇴근 기록부 및 현황
	AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date);

	//사용자 지각 현황
	List<LatenessDTO> getLateAttendanceList(String empId);
	
	//사용자 근무 조회
	List<AttendanceDetailDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate);

	//사용자 근태 항목
	List<AttendanceStatusDTO> getAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate);

	//사용자 휴가 내역 확인 및 신청
	List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate);
	int getRemainingLeaveDays(String empId);

	//관리자 출퇴근 기록부 조회 및 현황
	List<AttendanceDetailDTO> getAttendanceDetails(LocalDate startDate, LocalDate endDate);
	AttendanceDetailDTO getAttendanceDetailByEmpIdAndDate(String empId, LocalDate date);

	//관리자 휴가 일수 조회
	LeaveStatusDTO getLeaveStatusByEmpId(String empId);
	List<LeaveStatusDTO> getAllLeaveStatuses();

	
	//관리자 지각 현황 조회
	List<LatenessDTO> getLatenessByPeriodForAdmin(LocalDate startDate, LocalDate endDate);  
	
	//관리자 근무 형태 현황 조회
	List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate);  
	
	
	 // 관리자 근무 입력
    void insertWorkRecord(WorkInputDTO workInputDTO);

    // 관리자 근무 기록 수정 (출퇴근 시간 수정 등)
    void updateWorkRecord(WorkInputDTO workInputDTO);
}
