package com.itwill.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    // 근무 조회
    @Override
    public List<AttendanceDetailDTO> getAttendanceByEmpId(String empId) {
        return attendanceMapper.selectAttendanceByEmpId(empId);
    }

    // 출퇴근 기록부
    @Override
    public AttendanceDTO getTodayAttendance(String empId) {
        return attendanceMapper.selectTodayAttendance(empId);
    }

    // AttendanceDTO를 통해 근무 시간과 일수를 계산
    @Override
    public void calculateAttendance(AttendanceDTO attendanceDTO) {
        attendanceDTO.calculateWorkTime();
        attendanceDTO.calculateWorkDays();
    }

    // 예시로 근태 기록을 처리하는 메서드
    @Override
    public void processAttendance(String empId, LocalDateTime startTime, LocalDateTime endTime) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setEmpId(empId);
        attendanceDTO.setWorkStartTime(startTime);
        attendanceDTO.setWorkEndTime(endTime);

        // 근태 시간 및 일수 계산
        calculateAttendance(attendanceDTO);

        // 계산된 근무 시간과 일수 출력
        System.out.println("근무 시간: " + attendanceDTO.getWorkHours() + "시간");
        System.out.println("근무 일수: " + attendanceDTO.getWorkDays() + "일");

        // 기타 로직 처리...
    }

    // [16] 지각 여부 판단 및 처리
    @Override
    public AttendanceDetailDTO getAttendanceDetail(String empId, LocalDate date) {
        Attendance attendance = attendanceMapper.selectByEmpIdAndDate(empId, date);

        // 기준 시간 설정
        LocalTime standardStart = LocalTime.of(9, 0);  // 출근 시간 기준 (예시: 09:00)
        LocalTime standardEnd = LocalTime.of(18, 0);   // 퇴근 시간 기준 (예시: 18:00)

        // 기본 정보 세팅
        AttendanceDetailDTO dto = AttendanceDetailDTO.builder()
            .attendanceId(attendance.getAttendanceId())
            .empId(attendance.getEmpId())
            .checkInTime(attendance.getCheckInTime())
            .checkOutTime(attendance.getCheckOutTime())
            .isEarlyLeave(
                attendance.getCheckOutTime() != null &&
                attendance.getCheckOutTime().toLocalDateTime().toLocalTime().isBefore(standardEnd)
                ? "Y" : "N"
            )
            .build();

        // 지각 여부 판단
        String isLate = attendance.getCheckInTime().toLocalDateTime().toLocalTime().isAfter(standardStart) ? "Y" : "N";
        dto.setIsLate(isLate);

        // 지각 처리: 만약 지각이면 DB에 지각 정보 업데이트 (횟수 및 날짜)
        if ("Y".equals(isLate)) {
            // DB 업데이트: 지각 횟수 및 날짜 추가
            updateLateness(empId, attendance.getCheckInTime());
        }

        return dto;
    }

    // 지각 처리 메서드
    private void updateLateness(String empId, LocalDateTime checkInTime) {
        // 기존 지각 횟수 및 날짜를 조회
        LatenessDTO latenessDTO = latenessMapper.getLatenessInfo(empId);

        // 지각 횟수 업데이트
        int latenessCount = latenessDTO.getLatenessCount() + 1;  // 기존 횟수 + 1

        // 지각 날짜 업데이트
        String latenessDates = latenessDTO.getLatenessDates();
        String newLatenessDate = checkInTime.toLocalDate().toString();
        if (latenessDates == null) {
            latenessDates = newLatenessDate;
        } else {
            latenessDates = latenessDates + "," + newLatenessDate;  // 날짜 추가
        }

        // DB에 지각 정보 업데이트
        latenessMapper.updateLateness(empId, latenessCount, latenessDates);
    }

    // [4. 사용자 근무 상태 조회 - AttendanceStatusId]
    @Override
    public List<AttendanceStatusDTO> getAttendanceStatusList() {
        return attendanceMapper.selectAttendanceStatusList();
    }
}
