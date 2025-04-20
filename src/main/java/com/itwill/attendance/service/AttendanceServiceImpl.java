package com.itwill.attendance.service;

import com.itwill.attendance.dto.AttendanceDTO;
import com.itwill.attendance.dto.AttendanceDetailDTO;
import com.itwill.attendance.dto.LeaveStatusDTO;
import com.itwill.attendance.dto.WorkInputDTO;
import com.itwill.attendance.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;
    
    //사용자 출퇴근 기록부 및 현황
    @Override
    public AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date) {
        AttendanceDetailDTO detail = attendanceMapper.selectAttendanceDetail(empId, date);
        
        // ⚠️ DB에 없는 지각 여부 판단 로직 (출근 시간이 09:00 이후면 지각)
        LocalTime standardTime = LocalTime.of(9, 0);
        if (detail != null && detail.getCheckInTime() != null) {
            boolean isLate = detail.getCheckInTime().toLocalTime().isAfter(standardTime);
            detail.setIsLate(isLate ? "예" : "아니오");
        } else {
            detail.setIsLate("정보 없음");
        }

        return detail;
    }
    
    //사용자 지각 현황
    @Override
    public List<LatenessDTO> getLateAttendanceList(String empId) {
        List<LatenessDTO> allAttendances = attendanceMapper.selectLateAttendancesByEmpId(empId);

        return allAttendances.stream()
            .filter(dto -> dto.getCheckInTime() != null &&
                           dto.getCheckInTime().toLocalTime().isAfter(LocalTime.of(9, 0)))
            .map(dto -> {
                dto.setIsLate("예"); // 지각만 추출
                return dto;
            })
            .collect(Collectors.toList());
    }

    
    //사용자 근무 조회
    @Override
    public List<WorkRecordDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate) {
        List<WorkRecordDTO> records = attendanceMapper.selectWorkRecordsByEmpIdAndPeriod(empId, startDate, endDate);

        return records.stream().map(record -> {
            long minutes = Duration.between(record.getCheckInTime(), record.getCheckOutTime()).toMinutes();
            record.setWorkMinutes(minutes);
            return record;
        }).collect(Collectors.toList());
    }

    //사용자 근태 항목
    @Override
    public List<AttendanceStatusDTO> getAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceStatusByEmpIdAndPeriod(empId, startDate, endDate);
    }

    //사용자 휴가 내역 및 신청
    @Override
    public List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLeaveHistoryByEmpIdAndPeriod(empId, startDate, endDate);
    }

    @Override
    public int getRemainingLeaveDays(String empId) {
        return attendanceMapper.selectRemainingLeaveDays(empId);
    }

    
    //관리자 출퇴근 기록부 조회 및 현황
    @Override
    public List<AttendanceDetailDTO> getAttendanceDetails(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceDetails(startDate, endDate);
    }

    @Override
    public AttendanceDetailDTO getAttendanceDetailByEmpIdAndDate(String empId, LocalDate date) {
        return attendanceMapper.selectAttendanceDetailByEmpIdAndDate(empId, date);
    }

    //관리자 휴가 일수 조회
    @Override
    public LeaveStatusDTO getLeaveStatusByEmpId(String empId) {
        return leaveMapper.selectLeaveStatusByEmpId(empId);
    }

    @Override
    public List<LeaveStatusDTO> getAllLeaveStatuses() {
        return leaveMapper.selectAllLeaveStatuses();
    }

    //관리자 지각 현황 조회
    @Override
    public List<LatenessAdminDTO> getLatenessByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLatenessByPeriodForAdmin(startDate, endDate);
    }
    
    //관리자 근무 형태 현황 조회
    @Override
    public List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectWorkTypeByPeriodForAdmin(startDate, endDate);
    }
    
    // 관리자 근무 입력
    @Override
    public void insertWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.insertWorkRecord(workInputDTO);
    }

    // 관리자 근무 기록 수정 (출퇴근 시간 수정 등)
    @Override
    public void updateWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.updateWorkRecord(workInputDTO);
    }
}
