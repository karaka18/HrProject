package com.itwill.attendance.service;

import com.itwill.attendance.dto.AttendanceDTO;
import com.itwill.attendance.dto.AttendanceDetailDTO;
import com.itwill.attendance.dto.AttendanceStatusDTO;
import com.itwill.attendance.dto.LatenessAdminDTO;
import com.itwill.attendance.dto.LeaveDTO;
import com.itwill.attendance.dto.LeaveStatusDTO;
import com.itwill.attendance.dto.WorkInputDTO;
import com.itwill.attendance.dto.WorkRecordDTO;
import com.itwill.attendance.dto.WorkTypeAdminDTO;
import com.itwill.attendance.mapper.AttendanceMapper;
import com.itwill.attendance.mapper.LeaveMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    private final LeaveMapper leaveMapper;

    @Override
    public AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date) {
        AttendanceDetailDTO detail = attendanceMapper.selectAttendanceDetail(empId, date);

        LocalTime standardTime = LocalTime.of(9, 0);
        if (detail != null && detail.getCheckInTime() != null) {
            boolean isLate = detail.getCheckInTime().toLocalTime().isAfter(standardTime);
            detail.setIsLate(isLate ? "예" : "아니오");
        } else {
            detail.setIsLate("정보 없음");
        }

        return detail;
    }

    @Override
    public List getLateAttendanceList(String empId, LocalDate startDate, LocalDate endDate) {
        List<LatenessAdminDTO> allAttendances 
        = attendanceMapper.selectLateDetailsForAdmin(empId, startDate, endDate,departmentId);

        return allAttendances.stream()
            .filter(dto -> dto.getCheckInTime() != null &&
                           dto.getCheckInTime().toLocalTime().isAfter(LocalTime.of(9, 0)))
            .map(dto -> {
                dto.setIsLate("예");
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List getWorkRecords(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceDetailDTO> records 
        = attendanceMapper.selectWorkRecordsByEmpIdAndPeriod(empId, startDate, endDate);

        return records.stream().map(record -> {
            WorkRecordDTO workRecord = new WorkRecordDTO();
            workRecord.setEmpId(record.getEmpId());

            if (record.getCheckInTime() != null && record.getCheckOutTime() != null) {
                workRecord.setCheckInTime(record.getCheckInTime());
                workRecord.setCheckOutTime(record.getCheckOutTime());
                workRecord.setWorkMinutes(Duration.between(record.getCheckInTime(), record.getCheckOutTime()).toMinutes());
            } else {
                workRecord.setWorkMinutes(0L);
            }

            return workRecord;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceStatusDTO> getAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceStatusByEmpIdAndPeriod(empId, startDate, endDate);
    }

    @Override
    public List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLeaveHistoryByEmpIdAndPeriod(empId, startDate, endDate);
    }

    @Override
    public int getRemainingLeaveDays(String empId) {
        return attendanceMapper.selectRemainingLeaveDays(empId);
    }

    @Override
    public List<AttendanceDetailDTO> getAttendanceDetails(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceDetails(startDate, endDate);
    }

    @Override
    public AttendanceDetailDTO getAttendanceDetailByEmpIdAndDate(String empId, LocalDate date) {
        return attendanceMapper.selectAttendanceDetailByEmpIdAndDate(empId, date);
    }

    @Override
    public LeaveStatusDTO getLeaveStatusByEmpId(String empId) {
        return leaveMapper.selectLeaveStatusByEmpId(empId);
    }

    @Override
    public List<LeaveStatusDTO> getAllLeaveStatuses() {
        return leaveMapper.selectAllLeaveStatuses();
    }

    //@Override
    public List<LatenessAdminDTO> getLatenessByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        List<LatenessAdminDTO> allAttendances 
        = attendanceMapper.selectLateAttendancesByPeriodForAdmin(startDate, endDate);

        return allAttendances.stream()
                .filter(dto -> dto.getCheckInTime() != null &&
                               dto.getCheckInTime().toLocalTime().isAfter(LocalTime.of(9, 0)))
                .map(dto -> {
                    dto.setIsLate("예");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkTypeAdminDTO> getWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectWorkTypeByPeriodForAdmin(startDate, endDate);
    }

    @Override
    public void insertWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.insertWorkRecord(workInputDTO);
    }

    @Override
    public void updateWorkRecord(WorkInputDTO workInputDTO) {
        attendanceMapper.updateWorkRecord(workInputDTO);
        
    }
    
    @Override
    public boolean insert(WorkInputDTO workInputDTO) {
        // MyBatis 등을 통해 DB에 데이터를 삽입하는 로직 작성
        return attendanceMapper.insertWorkInput(workInputDTO) > 0;
    }

	@Override
	public List<LatenessAdminDTO> getLateAttendanceList(String empId) {
		// TODO Auto-generated method stub
		return null;
	}
}
