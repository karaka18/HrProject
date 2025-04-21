package com.itwill.attendance.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwill.attendance.dto.*;

public interface AttendanceMapper {

    List<AttendanceDTO> selectWorkRecords(String empId, LocalDate startDate, LocalDate endDate);

    List<LeaveDTO> selectLeaveHistory(String empId, LocalDate startDate, LocalDate endDate);

    int selectRemainingLeaveDays(String empId);

    List<AttendanceDTO> selectAttendanceDetails(String empId, LocalDate startDate, LocalDate endDate);
    
    
    //2. 사용자 지각 현황
    List<LateAttendanceDTO> selectLateAttendanceList(@Param("empId") String empId,
    		@Param("start") LocalDate start,
    		@Param("end") LocalDate end);

    //3. 사용자 근무 조회
    List<AttendanceSummaryDTO> selectWorkRecordsByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    //4. 사용자 근태 항목 조회
    AttendanceDetailDTO selectAttendanceDetail(@Param("empId") String empId, 
    		@Param("date") LocalDate date);
    
    
    List<AttendanceDTO> selectAttendanceDetailsByDateRange(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    String selectEmployeeNameById(@Param("empId") String empId);
    
    // 관리자 전용 기간별 근무유형 통계 조회
    List<WorkTypeAdminDTO> selectWorkTypeByPeriodForAdmin(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 관리자 전용 근무 기록 삽입
    void insertWorkRecord(WorkInputDTO workInputDTO);
    
    //관리자 전용 출근 기록 삽입 여부 확인
    int insertWorkInput(WorkInputDTO workInputDTO);
    
    //관리자 전용 근무 기록 수정
    void updateWorkRecord(WorkInputDTO workInputDTO);

    List<AttendanceStatusDTO> selectAttendanceStatusList(
    	    @Param("empId") String empId,
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);


    
}
