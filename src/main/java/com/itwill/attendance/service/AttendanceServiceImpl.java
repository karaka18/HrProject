package com.itwill.attendance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
	
    private final AttendanceMapper attendanceMapper;

    // [1] 사용자 출퇴근 기록 조회 + 현황
    @Override
    public List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyAttendanceRecord(empId, startDate, endDate);
    }

    // [2] 사용자 지각 현황 조회
    @Override
    public List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyLateness(empId, startDate, endDate);
    }

    // [3] 사용자 근무 요약 조회
    @Override
    public WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate) {
        return attendanceMapper.getWorkSummary(empId, startDate, endDate);
    }

    // [4] 사용자 근태 항목 및 근무 형태 조회
    @Override
    public List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyAttendanceStatus(empId, startDate, endDate);
    }

    // [5] 사용자 전체 휴가 내역 확인(모든 기간)
    @Override
    public List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId) {
        return attendanceMapper.getMyTotalLeaveHistory(empId);
    }

    // [5-1] 사용자 특정 기간 내의 휴가 내역 
    @Override
    public List<LeaveHistoryDTO> getMyLeaveHistoryByDate(String empId, String startDate, String endDate) {
    	return attendanceMapper.getMyLeaveHistoryByDate(empId, startDate, endDate);
    }
    
    
    // [6] 사용자 휴가 잔여 일수 확인
    @Override
    public LeaveBalanceDTO getMyLeaveBalance(String empId) {
        return attendanceMapper.getMyLeaveBalance(empId);
    }

    // [6-1] 사용자 휴가 신청 기능
    @Override
    public void applyForLeave(LeaveDTO leaveDTO) {
    	attendanceMapper.applyForLeave(leaveDTO);  // 휴가 신청 정보 DB에 저장
    }
    
    
    // [7] 관리자용 출퇴근 기록부 조회 (사원별/부서별/일자별)
    @Override
    public List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate) {
        return attendanceMapper.getAttendanceRecordsByCategory(empId, departmentId, startDate, endDate);
    }

    // [8] 관리자용 사원별 휴가 관리
    @Override
    public void updateLeaveDays(LeaveUpdateRequestDTO dto) {
        attendanceMapper.updateLeaveDays(dto);
    }

    // [9] 관리자용 지각 현황 조회 및 경고
    @Override
    public List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate) {
        return attendanceMapper.getAllLatenessRecords(empId, departmentId, startDate, endDate);
    }

    // [10] 관리자용 근무 형태 및 시간 조회
    @Override
    public List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate) {
        return attendanceMapper.getAllWorkStatus(empId, startDate, endDate);
    }

    // [11] 관리자용 근무 입력/수정/삭제 기능
    @Override
    public void updateAttendanceRecord(AttendanceUpdateDTO dto) {
        attendanceMapper.updateAttendanceRecord(dto);
    }

    // [12] 관리자용 근태 종합 조회
    @Override
    public List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate) {
        return attendanceMapper.getAttendanceSummaryForAdmin(startDate, endDate);
    }

    // [13] 출근 기능
    @Override
    public void clockIn(String empId) {
        AttendanceDTO attendanceDTO = AttendanceDTO.builder()
            .empId(empId)
            .status("출근")
            .clockIn(LocalDateTime.now())
            .build();

        attendanceMapper.insertAttendance(attendanceDTO);
    }

    // [14] 퇴근 기능
    @Override
    public void clockOut(String empId) {
        AttendanceDTO attendanceDTO = AttendanceDTO.builder()
            .empId(empId)
            .status("퇴근")
            .clockOut(LocalDateTime.now())
            .build();

        attendanceMapper.updateAttendance(attendanceDTO);
    }

    // [15] 출퇴근 기록 등록 기능 (출근/퇴근 자동 분기)
    @Override
    public void registerAttendance(AttendanceDTO attendanceDTO) {
        if ("출근".equals(attendanceDTO.getStatus())) {
            attendanceMapper.insertAttendance(attendanceDTO);
        } else if ("퇴근".equals(attendanceDTO.getStatus())) {
            attendanceMapper.updateAttendance(attendanceDTO);
        }
    }

    
}
