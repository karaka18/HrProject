package com.itwill.attendance.mapper;

import java.time.LocalDate;
import java.util.List;
import com.itwill.attendance.dto.*;

public interface AttendanceMapper {

    AttendanceDTO selectAttendanceDetail(String empId, LocalDate date);

    List<LatenessAdminDTO> selectLateAttendanceList(String empId, LocalDate start, LocalDate end);

    List<AttendanceDTO> selectWorkRecords(String empId, LocalDate startDate, LocalDate endDate);

    List<LeaveDTO> selectLeaveHistory(String empId, LocalDate startDate, LocalDate endDate);

    int selectRemainingLeaveDays(String empId);

    List<AttendanceDTO> selectAttendanceDetails(String empId, LocalDate startDate, LocalDate endDate);

    List<AttendanceStatusDTO> selectAttendanceStatus(String empId, LocalDate startDate, LocalDate endDate);

    List<WorkTypeAdminDTO> selectWorkTypeByPeriodForAdmin(LocalDate startDate, LocalDate endDate);

    void insertWorkRecord(WorkInputDTO workInputDTO);

    void updateWorkRecord(WorkInputDTO workInputDTO);

    int insertWorkInput(WorkInputDTO workInputDTO);
}
