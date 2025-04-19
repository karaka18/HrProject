package com.itwill.attendance.dto;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ✅ 출근 처리 API
    @PostMapping("/clock-in")
    @ResponseBody
    public void clockIn(@RequestBody Map<String, String> request) {
        String empId = request.get("empId");
        attendanceService.clockIn(empId);
    }

    // ✅ 퇴근 처리 API
    @PostMapping("/clock-out")
    @ResponseBody
    public void clockOut(@RequestBody Map<String, String> request) {
        String empId = request.get("empId");
        attendanceService.clockOut(empId);
    }

    // ✅ 메인 페이지 (오늘의 출근/퇴근 시간 확인용)
    @GetMapping("/main")
    public String showAttendanceMainPage(Model model, HttpSession session) {
        String empId = (String) session.getAttribute("empId");

        AttendanceDTO todayAttendance = attendanceService.getTodayAttendance(empId);

        model.addAttribute("todayAttendance", todayAttendance);
        model.addAttribute("loginUser", session.getAttribute("loginUser"));
        model.addAttribute("empId", empId);

        return "attendance/attendance-main";
    }

    // ✅ 출퇴근 기록 조회
    @GetMapping("/records")
    public String viewAttendanceRecords(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpSession session,
            Model model) {

        String empId = (String) session.getAttribute("empId");
        List<AttendanceDetailDTO> attendanceList = attendanceService.getMyAttendanceRecord(empId, startDate, endDate);

        model.addAttribute("attendanceList", attendanceList);
        return "attendance/attendance-summary";
    }

    // ✅ 상세 근태 정보 조회 (지각/조퇴 포함)
    @GetMapping("/detail/{empId}/{date}")
    @ResponseBody
    public AttendanceDetailDTO getAttendanceDetail(@PathVariable String empId, @PathVariable String date) {
        LocalDate attendanceDate = LocalDate.parse(date);
        return attendanceService.getAttendanceDetail(empId, attendanceDate);
    }

    // ✅ 사용자 휴가 내역 페이지
    @GetMapping("/leave")
    public String showLeaveHistory(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            HttpSession session,
            Model model) {

        String empId = (String) session.getAttribute("empId");
        List<LeaveHistoryDTO> leaveHistory = attendanceService.getMyLeaveHistoryByDate(empId, startDate, endDate);

        model.addAttribute("leaveList", leaveHistory);
        return "attendance/attendance-leave";
    }

    // ✅ 휴가 신청 API
    @PostMapping("/leave/apply")
    @ResponseBody
    public void applyForLeave(@RequestBody LeaveDTO leaveDTO) {
        attendanceService.applyForLeave(leaveDTO);
    }

    // ✅ 연차 잔여일수 확인
    @GetMapping("/leave-balance")
    @ResponseBody
    public LeaveBalanceDTO getLeaveBalance(@RequestParam String empId) {
        return attendanceService.getMyLeaveBalance(empId);
    }

    // ✅ 관리자 - 사원별 출근기록 조회
    @GetMapping("/admin/records")
    @ResponseBody
    public List<AttendanceDetailDTO> getAdminRecords(
            @RequestParam(required = false) String empId,
            @RequestParam(required = false) String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return attendanceService.getAttendanceRecordsByCategory(empId, departmentId, startDate, endDate);
    }

    // ✅ 관리자 - 근무정보 수정
    @PostMapping("/admin/work/update")
    @ResponseBody
    public void updateAttendance(@RequestBody AttendanceUpdateDTO dto) {
        attendanceService.updateAttendanceRecord(dto);
    }

    // ✅ 관리자 - 종합 근태 요약
    @GetMapping("/admin/summary")
    @ResponseBody
    public List<AttendanceWarningDTO> getAttendanceSummary(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getAttendanceSummaryForAdmin(startDate, endDate);
    }

    // ✅ 근무 상태(지각/결근/정상 등) 조회
    @GetMapping("/status")
    @ResponseBody
    public List<AttendanceStatusDTO> getStatusList(
            @RequestParam String empId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceService.getMyAttendanceStatus(empId, startDate, endDate);
    }

    // ✅ 근무 형태 조회 페이지 (JSP)
    @GetMapping("/attendance/status")
    public String showAttendanceStatusPage(Model model) {
        List<AttendanceStatusDTO> list = attendanceService.getAttendanceStatusList();
        model.addAttribute("statusList", list);
        return "attendance/attendance-status";
    }
}