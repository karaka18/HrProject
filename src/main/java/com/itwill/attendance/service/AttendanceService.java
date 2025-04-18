package com.itwill.attendance.service;

import java.util.List;

import com.itwill.attendance.dto.AttendanceDTO;
import com.itwill.attendance.dto.AttendanceDetailDTO;
import com.itwill.attendance.dto.AttendanceStatusDTO;
import com.itwill.attendance.dto.AttendanceUpdateDTO;
import com.itwill.attendance.dto.AttendanceWarningDTO;
import com.itwill.attendance.dto.LatenessDTO;
import com.itwill.attendance.dto.LeaveBalanceDTO;
import com.itwill.attendance.dto.LeaveDTO;
import com.itwill.attendance.dto.LeaveHistoryDTO;
import com.itwill.attendance.dto.LeaveUpdateRequestDTO;
import com.itwill.attendance.dto.WorkSummaryDTO;

public interface AttendanceService {

    // [1] 사용자 출퇴근 기록 조회 + 현황
    List<AttendanceDetailDTO> getMyAttendanceRecord(String empId, String startDate, String endDate);

    // [2] 사용자 지각 현황 조회
    List<LatenessDTO> getMyLateness(String empId, String startDate, String endDate);

    // [3] 사용자 근무 요약 조회
    WorkSummaryDTO getWorkSummary(String empId, String startDate, String endDate);

    // [4] 사용자 근태 항목 및 근무 형태 조회
    List<AttendanceStatusDTO> getMyAttendanceStatus(String empId, String startDate, String endDate);

    // [5] 사용자 전체 휴가 내역 확인(모든 기간)
    List<LeaveHistoryDTO> getMyTotalLeaveHistory(String empId);

    // [5-1] 사용자 특정 기간 내 휴가 신청 내역 + 보고서 다운로드 등
    List<LeaveHistoryDTO> getMyLeaveHistoryByDate(String empId, String startDate, String endDate);
    
    // [6] 사용자 휴가 잔여 일수 확인 (단순 수치만 반환)
    LeaveBalanceDTO getMyLeaveBalance(String empId);
    
    // [6-1] 사용자 휴가 신청
    void applyForLeave(LeaveDTO leaveDTO);

    // [7] 관리자용 출퇴근 기록부 조회 (사원별/부서별/일자별)
    List<AttendanceDetailDTO> getAttendanceRecordsByCategory(String empId, String departmentId, String startDate, String endDate);

    // [8] 관리자용 사원별 휴가 일수 수정
    void updateLeaveDays(LeaveUpdateRequestDTO dto);

    // [9] 관리자용 지각 현황 조회 및 경고 관리
    List<LatenessDTO> getAllLatenessRecords(String empId, String departmentId, String startDate, String endDate);

    // [10] 관리자용 근무 형태 및 시간 조회
    List<AttendanceStatusDTO> getAllWorkStatus(String empId, String startDate, String endDate);

    // [11] 관리자용 근무 정보 입력/수정/삭제
    void updateAttendanceRecord(AttendanceUpdateDTO dto);

    // [12] 관리자용 근태 종합 경고/요약 현황 조회
    List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(String startDate, String endDate);

    // [13] 사용자 출근 기록 등록
    void registerAttendance(AttendanceDTO attendanceDTO);

    // [14] 사용자 출근 처리
    void clockIn(String empId);  

    // [15] 사용자 퇴근 처리
    void clockOut(String empId); 
    
    List<AttendanceDetailDTO> getAttendanceByEmpId(String empId);
    
    AttendanceDTO getTodayAttendance(String empId);
    
 // 근태 계산 메서드
    public void calculateAttendance(AttendanceDTO attendanceDTO) {
        // 근무 시간 계산
        calculateWorkTime(attendanceDTO);

        // 근무 일수 계산
        calculateWorkDays(attendanceDTO);
    }

    // 근무 시간 계산 (출근 시간과 퇴근 시간 기반으로)
    private void calculateWorkTime(AttendanceDTO attendanceDTO) {
        if (attendanceDTO.getWorkStartTime() != null && attendanceDTO.getWorkEndTime() != null) {
            Duration duration = Duration.between(attendanceDTO.getWorkStartTime(), attendanceDTO.getWorkEndTime());
            attendanceDTO.setWorkHours((int) duration.toHours()); // 근무 시간 설정
        }
    }

    // 근무 일수 계산 (출근일과 퇴근일의 차이 계산)
    private void calculateWorkDays(AttendanceDTO attendanceDTO) {
        if (attendanceDTO.getWorkStartTime() != null && attendanceDTO.getWorkEndTime() != null) {
            long daysBetween = ChronoUnit.DAYS.between(attendanceDTO.getWorkStartTime().toLocalDate(), attendanceDTO.getWorkEndTime().toLocalDate());
            attendanceDTO.setWorkDays((int) daysBetween + 1); // 근무 일수 설정 (하루를 포함)
        }
    }

    // 근무 기록을 저장하는 메서드
    public void registerAttendance(AttendanceDTO attendanceDTO) {
        calculateAttendance(attendanceDTO);  // 근무 시간 및 근무 일수 계산
        attendanceMapper.insertAttendance(attendanceDTO);  // DB에 저장
    }

    // 출퇴근 기록을 업데이트하는 메서드
    public void updateAttendance(AttendanceUpdateDTO attendanceUpdateDTO) {
        // 수정 시 수정 시간을 갱신
        String updatedAt = getCurrentTimestamp();
        attendanceUpdateDTO.setUpdatedAt(updatedAt);

        attendanceMapper.updateAttendance(attendanceUpdateDTO);
    }

    // 현재 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 반환하는 메서드
    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // DB에서 근무 일수와 근무 시간 계산
    public void calculateWorkDetails(String empId) {
        // 근무 일자 (출근한 날 개수)
        int workDays = attendanceMapper.countWorkDays(empId);

        // 근무 시간 (출근한 날의 근무 시간 합)
        double workHours = attendanceMapper.calculateWorkHours(empId);

        // 근무 일자와 근무 시간 출력 (로그 혹은 다른 곳에서 사용)
        System.out.println("근무 일자: " + workDays);
        System.out.println("근무 시간: " + workHours);
    }
    
    public void recordAttendance(String empId, String configuredStartTime, String actualArrivalTime) {
        // 출근 시간 비교 후 지각 처리
        checkLateness(empId, configuredStartTime, actualArrivalTime);
        
        // 출근 기록 저장
        Attendance attendance = new Attendance();
        attendance.setEmpId(empId);
        attendance.setArrivalTime(actualArrivalTime);
        // 기타 출근 기록 처리

        attendanceRepository.save(attendance); // DB에 저장
    }

    public void checkLateness(String empId, String configuredStartTime, String actualArrivalTime) {
        // 출근 시간 (예: 9:00)
        LocalTime startTime = LocalTime.parse(configuredStartTime);
        // 실제 출근 시간 (예: 9:05)
        LocalTime arrivalTime = LocalTime.parse(actualArrivalTime);

        // 실제 출근 시간이 설정된 출근 시간보다 늦으면 지각 처리
        if (arrivalTime.isAfter(startTime)) {
            long latenessMinutes = Duration.between(startTime, arrivalTime).toMinutes();
            
            // 지각 시간과 횟수 업데이트 로직
            updateLateness(empId, latenessMinutes);
        }
    }

    // 지각 정보 업데이트 예시 (DB에 저장)
    private void updateLateness(String empId, long latenessMinutes) {
        // DB에 지각 시간과 관련 정보를 업데이트
        // 예: latenessCount 증가, latenessDates에 날짜 추가 등
        // DB에 업데이트하는 로직은 Repository를 사용하여 작성
    }
    
    /**
    * 근태 상세 조회 (지각 여부 포함)
    * @param empId 사원 ID
    * @param date 조회할 날짜
    * @return 근태 상세 정보
    */
   public AttendanceDetailDTO getAttendanceDetail(String empId, LocalDate date) {
       // 근태 상세 정보를 조회 후 반환
       AttendanceDetailDTO dto = attendanceMapper.getAttendanceDetail(empId, date);
       
       // 지각 여부 판단 후 DTO에 세팅
       return dto;
   }

   /**
    * 지각 횟수 및 날짜 조회
    * @param empId 사원 ID
    * @return 지각 횟수 및 날짜
    */
   public LatenessDTO getLatenessInfo(String empId) {
       // 지각 횟수 및 날짜를 조회
       return latenessMapper.getLatenessInfo(empId);
   }
}
