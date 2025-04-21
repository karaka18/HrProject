package com.itwill.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.mapper.AttendanceMapper;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    // ✅ 1. 출퇴근 기록 상세 조회
    @Override
    public AttendanceDetailDTO getAttendanceDetailDTO(String empId, LocalDate date) {
        AttendanceDTO attendanceDTO = attendanceMapper.selectAttendanceDetail(empId, date);
        return attendanceDTO != null ? attendanceDTO.toAttendanceDetailDTO() : null;
    }

    // ✅ 2. 지각 기록 리스트 (관리자용)
    @Override
    public List<LatenessAdminDTO> getLateAttendanceList(String empId, LocalDate start, LocalDate end) {
        return attendanceMapper.selectLateAttendanceList(empId, start, end);
    }

    // ✅ 3. 근무 내역 조회
    @Override
    public List<AttendanceDetailDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceDTO> attendanceDTOList = attendanceMapper.selectAttendanceDetails(empId, startDate, endDate);
        return attendanceDTOList.stream()
                .map(AttendanceDTO::toAttendanceDetailDTO)
                .collect(Collectors.toList());
    }

    // ✅ 4. 휴가 내역 조회
    @Override
    public List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLeaveHistory(empId, startDate, endDate);
    }

    // ✅ 5. 잔여 휴가 일수
    @Override
    public int getRemainingLeaveDays(String empId) {
        return attendanceMapper.selectRemainingLeaveDays(empId);
    }

    // ✅ 6. 출결 상태 통계
    @Override
    public List<AttendanceStatusDTO> getAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceStatus(empId, startDate, endDate);
    }

    // ✅ 7. 관리자용 기간별 근무유형 통계
    @Override
    public List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectWorkTypeByPeriodForAdmin(startDate, endDate);
    }

    // ✅ 8. 출근 기록 입력
    @Override
    public void insertWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.insertWorkRecord(workInputDTO);
    }

    // ✅ 9. 퇴근 기록 수정
    @Override
    public void updateWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.updateWorkRecord(workInputDTO);
    }

    // ✅ 10. 출근 기록 삽입 여부 확인
    @Override
    public boolean insert(WorkInputDTO workInputDTO) {
        return attendanceMapper.insertWorkInput(workInputDTO) > 0;
    }

    // ✅ AttendanceDTO → AttendanceDetailDTO 변환 메서드
    @Override
    public List<AttendanceDetailDTO> getAttendanceDetails(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceDTO> attendanceDTOList = attendanceMapper.selectAttendanceDetails(empId, startDate, endDate);
        return attendanceDTOList.stream()
                .map(AttendanceDTO::toAttendanceDetailDTO)
                .collect(Collectors.toList());
    }
}
