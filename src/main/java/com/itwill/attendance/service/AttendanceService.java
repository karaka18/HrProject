package com.itwill.attendance.service;

public interface AttendanceService{
	
	//사용자 출퇴근 기록부 및 현황
	AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date);

	//사용자 지각 현황
	List<LateAttendanceDTO> getLateAttendanceList(String empId);
	
	//사용자 근무 조회
	List<WorkRecordDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate);

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
	List<LatenessAdminDTO> getLatenessByPeriodForAdmin(LocalDate startDate, LocalDate endDate);  
	
	//관리자 근무 형태 현황 조회
	List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate);  
	
	
	 // 관리자 근무 입력
    void insertWorkRecord(WorkInputDTO workInputDTO);

    // 관리자 근무 기록 수정 (출퇴근 시간 수정 등)
    void updateWorkRecord(WorkInputDTO workInputDTO);
	
	
	
	
	
	
	
	
//1. 출퇴근 기록
void checkIn(String empId);
void checkOut(String empId);
AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date);

// 2. 지각 현황
LatenessDTO getLatenessByDate(String empId, LocalDate date);
List<LatenessDTO> getLatenessByPeriod(String empId, LocalDate startDate, LocalDate endDate);

// 3. 근무 조회 (출장, 야근, 휴일 근무 등)
WorkSummaryDTO getWorkSummary(String empId, LocalDate startDate, LocalDate endDate, String category);

// 4. 근태 항목 조회
List<AttendanceStatusDTO> getWorkTypeByDate(String empId, LocalDate startDate, LocalDate endDate, String category);

// 5. 휴가 내역
List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate);
int getRemainingLeaveDays(String empId);
void requestLeave(LeaveDTO leaveDTO);
String getLeaveReportFilePath(String leaveId); // PDF/엑셀 양식

//관리자 - 전체/부서/카테고리별 출퇴근 기록 조회
List<AttendanceDetailDTO> getAttendanceRecordsByFilter(String departmentId, String category, LocalDate startDate, LocalDate endDate);

//관리자 - 출퇴근 기록 수정
void updateAttendanceRecord(AttendanceDetailDTO attendanceDetailDTO);

//관리자 - 출퇴근 기록 삭제
void deleteAttendanceRecord(String attendanceId);


}
