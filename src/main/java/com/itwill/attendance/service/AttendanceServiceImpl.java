package com.itwill.attendance.service;

import java.time.LocalDate;
import java.util.List;
import com.itwill.attendance.dto.*;
import com.itwill.attendance.mapper.AttendanceMapper;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceDetailDTO getDailyAttendance(String empId, LocalDate date) {
        return attendanceMapper.selectAttendanceDetail(empId, date);
    }

    @Override
    public List<LatenessAdminDTO> getLateAttendanceList(String empId, LocalDate start, LocalDate end) {
        return attendanceMapper.selectLateAttendanceList(empId, start, end);
    }

    @Override
    public List<AttendanceDetailDTO> getWorkRecords(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectWorkRecords(empId, startDate, endDate);
    }

    @Override
    public List<LeaveDTO> getLeaveHistory(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectLeaveHistory(empId, startDate, endDate);
    }

    @Override
    public int getRemainingLeaveDays(String empId) {
        return attendanceMapper.selectRemainingLeaveDays(empId);
    }

    @Override
    public List<AttendanceDetailDTO> getAttendanceDetails(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceDetails(empId, startDate, endDate);
    }

    @Override
    public List<AttendanceStatusDTO> getAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate) {
        return attendanceMapper.selectAttendanceStatus(empId, startDate, endDate);
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
        return attendanceMapper.insertWorkInput(workInputDTO) > 0;
    }
}
