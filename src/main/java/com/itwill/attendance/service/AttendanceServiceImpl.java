package com.itwill.attendance.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.mapper.AttendanceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    // ✅ 출근 처리
    @Override
    public void clockIn(String empId) {
        // 이미 출근했는지 확인
        if (attendanceMapper.hasCheckedInToday(empId)) return;

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 출근 기록 생성
        AttendanceDTO dto = AttendanceDTO.builder()
            .attendanceId(UUID.randomUUID().toString())
            .empId(empId)
            .workDate(Timestamp.valueOf(now))
            .checkInTime(Time.valueOf(now.toLocalTime()))
            .createdAt(Timestamp.valueOf(now))
            .build();

        attendanceMapper.insertAttendance(dto);
    }

    // ✅ 퇴근 처리
    @Override
    public void clockOut(String empId) {
        LocalDateTime now = LocalDateTime.now();
        attendanceMapper.updateClockOut(empId, now);
    }

    // ✅ 오늘의 출근/퇴근 데이터 가져오기
    @Override
    public AttendanceDTO getTodayAttendance(String empId) {
        return attendanceMapper.selectTodayAttendance(empId);
    }

    // ✅ 날짜별 상세 조회 (출근시간, 퇴근시간, 지각/조퇴 판단 포함)
    @Override
    public AttendanceDetailDTO getAttendanceDetail(String empId, LocalDate date) {
        AttendanceDetailDTO dto = attendanceMapper.getAttendanceDetail(empId, date);

        if (dto == null) return null;

        // 지각 기준: 09:00
        if (dto.getCheckInTime() != null && dto.getCheckInTime().toLocalDateTime().toLocalTime().isAfter(java.time.LocalTime.of(9, 0))) {
            dto.setIsLate("Y");
        } else {
            dto.setIsLate("N");
        }

        // 조퇴 기준: 18:00
        if (dto.getCheckOutTime() != null && dto.getCheckOutTime().toLocalDateTime().toLocalTime().isBefore(java.time.LocalTime.of(18, 0))) {
            dto.setIsEarlyLeave("Y");
        } else {
            dto.setIsEarlyLeave("N");
        }

        return dto;
    }

    // ✅ 사원별 전체 출퇴근 기록
    @Override
    public List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyAttendanceRecord(empId, startDate, endDate);
    }

    @Override
    public List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyLateness(empId, startDate, endDate);
    }

    @Override
    public WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate) {
        return attendanceMapper.getWorkSummary(empId, startDate, endDate);
    }

    @Override
    public List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyAttendanceStatus(empId, startDate, endDate);
    }

    @Override
    public List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate) {
        return attendanceMapper.getAttendanceRecordsByCategory(empId, departmentId, startDate, endDate);
    }

    @Override
    public void updateAttendanceRecord(AttendanceUpdateDTO dto) {
        attendanceMapper.updateAttendanceRecord(dto);
    }

    @Override
    public List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate) {
        return attendanceMapper.getAllLatenessRecords(empId, departmentId, startDate, endDate);
    }

    @Override
    public List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate) {
        return attendanceMapper.getAllWorkStatus(empId, startDate, endDate);
    }

    @Override
    public List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate) {
        return attendanceMapper.getAttendanceSummaryForAdmin(startDate, endDate);
    }

    @Override
    public List<AttendanceDetailDTO> getAttendanceByEmpId(String empId) {
        return attendanceMapper.selectAttendanceByEmpId(empId);
    }

    @Override
    public List<AttendanceStatusDTO> getAttendanceStatusList() {
        return attendanceMapper.selectAttendanceStatusList();
    }

    @Override
    public List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId) {
        return attendanceMapper.getMyTotalLeaveHistory(empId);
    }

    @Override
    public List<LeaveHistoryDTO> getMyLeaveHistoryByDate(String empId, String startDate, String endDate) {
        return attendanceMapper.getMyLeaveHistoryByDate(empId, startDate, endDate);
    }

    @Override
    public LeaveBalanceDTO getMyLeaveBalance(String empId) {
        return attendanceMapper.getMyLeaveBalance(empId);
    }

    @Override
    public void applyForLeave(LeaveDTO leaveDTO) {
        attendanceMapper.applyForLeave(leaveDTO);
    }

    @Override
    public void updateLeaveDays(LeaveUpdateRequestDTO dto) {
        attendanceMapper.updateLeaveDays(dto);
    }
}