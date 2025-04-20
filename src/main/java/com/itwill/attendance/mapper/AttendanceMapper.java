package com.itwill.attendance.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwill.attendance.dto.*;

public interface AttendanceMapper {
	
	// 사용자 출퇴근 기록 (일자별 1건), (+조회기능 추가하기)
    AttendanceDetailDTO selectAttendanceDetail(@Param("empId") String empId, @Param("date") LocalDate date);
	
    //사용자 지각 현황
    List<LatenessDTO> selectLateAttendancesByEmpId(String empId);

    //사용자 근무 조회
    List<AttendanceStatusDTO> selectWorkRecordsByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    //사용자 근태 항목
    List<AttendanceStatusDTO> selectAttendanceStatusByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    
    //사용자 휴가 내역 확인 및 신청
    List<LeaveDTO> selectLeaveHistoryByEmpIdAndPeriod(@Param("empId") String empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

int selectRemainingLeaveDays(@Param("empId") String empId);

    
//관리자 출퇴근 기록부 및 현황 조회
List<AttendanceDetailDTO> selectAttendanceDetails(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

AttendanceDetailDTO selectAttendanceDetailByEmpIdAndDate(@Param("empId") String empId,
                @Param("date") LocalDate date);


//관리자 휴가 일수 조회
//사원별 휴가 상태 조회
LeaveStatusDTO selectLeaveStatusByEmpId(String empId);

//전체 사원의 휴가 상태 조회
List<LeaveStatusDTO> selectAllLeaveStatuses();

//지각 현황 조회 (관리자용)
List<LatenessDTO> selectLateDetailsForAdmin(@Param("startDate") String startDate,
                                            @Param("endDate") String endDate,
                                            @Param("departmentId") String departmentId);


//근무 형태 통계 조회 (관리자용) - 부서별, 날짜별 카운트
List<WorkSummaryDTO> selectWorkSummaryForAdmin(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("departmentId") String departmentId);
}