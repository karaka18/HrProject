package com.itwill.attendance.service;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Duration;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.mapper.AttendanceMapper;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    //사용자(사원) 전용 
    // 1. 사원 이름 조회
    @Override
    public String getEmployeeNameById(String empId) {
        return attendanceMapper.selectEmployeeNameById(empId);
    }

    // 2. 출퇴근 상세 조회 (단일 날짜)
    @Override
    public AttendanceDetailDTO getAttendanceDetailDTO(String empId, LocalDate date) {
        AttendanceDetailDTO dto = attendanceMapper.selectAttendanceDetail(empId, date);
        return dto;
    }

    // 3. 출퇴근 상세 조회 (기간 범위)
    @Override
    public AttendanceDetailDTO getAttendanceDetails(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceDTO> attendanceDTOList = attendanceMapper.selectAttendanceDetailsByDateRange(empId, startDate, endDate);
        if (attendanceDTOList != null && !attendanceDTOList.isEmpty()) {
            return attendanceDTOList.stream()
                                    .map(AttendanceDTO::toAttendanceDetailDTO)
                                    .findFirst()
                                    .orElse(null);
        }
        return null;
    }

    // 4. 사용자 지각 현황
    @Override
    public List<LateAttendanceDTO> getLateAttendanceList(String empId, LocalDate start, LocalDate end) {
        return attendanceMapper.selectLateAttendanceList(empId, start, end); 
    }

    // 5. 사용자 근무 이력 조회
    @Override
    public List<AttendanceSummaryDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceSummaryDTO> attendanceSummaryList = attendanceMapper.selectWorkRecordsByEmpIdAndPeriod(empId, startDate, endDate);
        return attendanceSummaryList.stream()
                .map(record -> AttendanceSummaryDTO.builder()
                        .empId(record.getEmpId())
                        .empName(record.getEmpName())
                        .workDate(record.getWorkDate())
                        .checkInTime(record.getCheckInTime())
                        .checkOutTime(record.getCheckOutTime())
                        .isLate(record.getIsLate())  
                        .isEarlyLeave(record.getIsEarlyLeave()) 
                        .workHours(calculateWorkHours(record.getCheckInTime(), record.getCheckOutTime())) 
                        .build())
                .collect(Collectors.toList());
    }

    // 근무 시간 계산
    private Double calculateWorkHours(LocalTime checkInTime, LocalTime checkOutTime) {
        if (checkInTime != null && checkOutTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            long totalMinutes = duration.toMinutes();
            return totalMinutes / 60.0;
        }
        return 0.0;
    }

    // 6. 사용자 근태 항목 조회
    @Override
    public AttendanceDetailDTO getAttendanceDetailByEmpIdAndDate(String empId, LocalDate date) {
        return attendanceMapper.selectAttendanceDetail(empId, date);
    }

    // 7. 휴가 내역 조회
    @Override
    public List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLeaveHistory(empId, startDate, endDate);
    }

    // 8. 잔여 휴가 일수 조회
    @Override
    public int getRemainingLeaveDays(String empId) {
        return attendanceMapper.selectRemainingLeaveDays(empId);
    }

    
    //관리자 전용
   
    // 관리자 전용 기간별 근무유형 출결 통계 조회
    @Override
    public List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        
        return attendanceMapper.selectWorkTypeByPeriodForAdmin(startDate, endDate);
    }
    
    // 관리자 전용  근무 기록 삽입
    @Override
    public void insertWorkRecord(WorkInputDTO workInputDTO) {
        
        attendanceMapper.insertWorkRecord(workInputDTO);
    }
    
    // 관리자 전용 출근 기록 삽입 여부 확인 (boolean 반환)
    @Override
    public boolean insert(WorkInputDTO workInputDTO) {
    	return attendanceMapper.insertWorkInput(workInputDTO) > 0;
    }

    // 관리자 전용 퇴근 기록 수정
    @Override
    public void updateWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.updateWorkRecord(workInputDTO);
    }
    
  

 


    
}
