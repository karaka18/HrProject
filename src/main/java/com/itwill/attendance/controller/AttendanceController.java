package com.itwill.attendance.controller;

import javax.servlet.http.HttpSession;  // HttpSession 추가


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.itwill.attendance.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private HttpSession session;  // HttpSession 객체를 주입받습니다.

    /**
     * [1. 사용자 출퇴근 기록 조회 + 현황]
     * 사용자가 기간별 출퇴근 기록을 조회하고, 출근/퇴근 여부 및 상태를 확인할 수 있음
     */
    @GetMapping("/main")
    public List<AttendanceDetailDTO> getMyAttendanceRecord(
            @RequestParam String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getMyAttendanceRecord(empId, startDate, endDate);
    }

    /**
     * [2. 사용자 지각 현황 조회]
     * 사용자가 기간별 지각 횟수, 지각 시간, 지각 사유서와 승인 상태 등을 확인할 수 있음
     */
    @GetMapping("/lateness")
    public List<LatenessDTO> getMyLateness(
            @RequestParam String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getMyLateness(empId, startDate, endDate);
    }

    /**
     * [3. 사용자 근무 요약 조회]
     * 사용자가 기간별 누적 근무 일수, 누적 근무 시간 등의 요약 정보를 조회할 수 있음
     */
    @GetMapping("/work-summary")
    public WorkSummaryDTO getMyWorkSummary(
            @RequestParam String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getWorkSummary(empId, startDate, endDate);
    }

    /**
     * [4. 사용자 근태 항목 및 근무 형태 조회]
     * 사용자가 자신의 근무, 지각, 결근, 외근 등 근태 유형 및 일자별 내역을 확인할 수 있음
     */
    @GetMapping("/status")
    public List<AttendanceStatusDTO> getMyAttendanceStatus(
            @RequestParam String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getMyAttendanceStatus(empId, startDate, endDate);
    }

    /**
     * [5. 사용자 휴가 내역 조회]
     * 사용자가 자신이 신청한 휴가 기록과 승인 현황을 확인하고, 다운로드할 수 있음
     */
    @GetMapping("/leave-history")
    public List<LeaveHistoryDTO> getMyLeaveHistory(@RequestParam String empId) {
        return attendanceService.getMyTotalLeaveHistory(empId);  // LeaveHistoryDTO 반환
    }
    
    /**
     * [5-1. 사용자 특정 기간 내의 휴가 내역 조회 ]
     */
    @RequestMapping("/attendance-leave")
    public String showLeaveHistory(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            Model model
    ) {
        String empId = (String) session.getAttribute("empId");
        
        // 기간별 휴가 내역 조회 시 LeaveHistoryDTO로 반환
        List<LeaveHistoryDTO> leaveHistory = attendanceService.getMyLeaveHistoryByDate(empId, startDate, endDate);
        
        model.addAttribute("leaveList", leaveHistory);
        
        return "attendance/attendance-leave";  // JSP 뷰 이름
    }

    /**
     * [6. 사용자 휴가 잔여 일수 확인]
     * 사용자가 현재 남은 연차, 사용한 연차 등의 정보를 확인할 수 있음
     */
    @GetMapping("/leave-balance")
    public LeaveBalanceDTO getMyLeaveBalance(
            @RequestParam String empId) {
        return attendanceService.getMyLeaveBalance(empId);
    }
    
    /**
     * [6-1. 사용자 휴가 신청 기능]
     */
    @PostMapping("/leave/apply")
    public void applyForLeave(@RequestBody LeaveDTO leaveDTO) {
        attendanceService.applyForLeave(leaveDTO);
    }
    

    /**
     * [7. 관리자용 출퇴근 기록부 조회 (사원별/부서별/일자별)]
     */
    @GetMapping("/admin/records")
    public List<AttendanceDetailDTO> getAttendanceRecordsByCategory(
            @RequestParam(required = false) String empId,
            @RequestParam(required = false) String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getAttendanceRecordsByCategory(empId, departmentId, startDate, endDate);
    }

    /**
     * [8. 관리자용 사원별 휴가 관리 (일수 조회, 추가, 삭제)]
     */
    @PostMapping("/admin/leave/update")
    public void updateLeaveDays(@RequestBody LeaveUpdateRequestDTO dto) {
        attendanceService.updateLeaveDays(dto);
    }

    /**
     * [9. 관리자용 지각 현황 조회 및 경고]
     */
    @GetMapping("/admin/lateness")
    public List<LatenessDTO> getAllLatenessRecords(
            @RequestParam(required = false) String empId,
            @RequestParam(required = false) String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getAllLatenessRecords(empId, departmentId, startDate, endDate);
    }

    /**
     * [10. 관리자용 근무 형태 및 시간 조회]
     */
    @GetMapping("/admin/work-form")
    public List<AttendanceStatusDTO> getAllWorkStatus(
            @RequestParam(required = false) String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getAllWorkStatus(empId, startDate, endDate);
    }

    /**
     * [11. 관리자용 근무 입력/수정/삭제 기능]
     */
    @PostMapping("/admin/work/update")
    public void updateAttendanceRecord(@RequestBody AttendanceUpdateDTO dto) {
        attendanceService.updateAttendanceRecord(dto);
    }

    /**
     * [12. 관리자용 근태 종합 조회 (지각/결근 기준 이상 시 경고)]
     */
    @GetMapping("/admin/summary")
    public List<AttendanceWarningDTO> getAttendanceSummaryForAdmin(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getAttendanceSummaryForAdmin(startDate, endDate);
    }

    @PostMapping("/attendance/clock-in")
    @ResponseBody
    public void clockIn(@RequestBody Map<String, String> request) {
        String empId = request.get("empId");
        attendanceService.clockIn(empId);
    }

    @PostMapping("/attendance/clock-out")
    @ResponseBody
    public void clockOut(@RequestBody Map<String, String> request) {
        String empId = request.get("empId");
        attendanceService.clockOut(empId);
    }

    // jsp뷰페이지 - 근태 관리 메인 페이지  (변경 X!)
    @RequestMapping("/attendance-main")
    public String showAttendanceMain(Model model) {
        return "attendance/attendance-main";  // /WEB-INF/views/attendance/attendance-main.jsp로 매핑
    }

    // jsp뷰페이지 - 근태관리 - 지각 현황 페이지로 이동
    @RequestMapping("/attendance-late")
    public String showLateAttendance(Model model, @RequestParam String startDate, @RequestParam String endDate) {
        // 세션에서 empId 가져오기 (현재 로그인한 사용자)
        String empId = (String) session.getAttribute("empId");

        // 사용자의 지각 내역 조회
        List<LatenessDTO> lateAttendanceList = attendanceService.getMyLateness(empId, startDate, endDate);

        // 조회된 지각 내역을 모델에 추가
        model.addAttribute("attendanceLateList", lateAttendanceList);

        return "attendance/attendance-late";  // JSP 파일 이름
    }
    

}
