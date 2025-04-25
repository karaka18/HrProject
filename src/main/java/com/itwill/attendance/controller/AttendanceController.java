package com.itwill.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // ✅ 출퇴근 메인 페이지
    @GetMapping("/main")
    public String showAttendanceMainPage(HttpSession session) {
        String empId = (String) session.getAttribute("id");
        if (empId == null) {
            return "redirect:/member/login";
        }
        return "attendance/attendance-main";
    }

    // ✅ 지각 현황 페이지
    @GetMapping("/late")
    public String showLatePage() {
        return "attendance/attendance-late";
    }

    // ✅ 근무 조회 페이지
    @GetMapping("/summary")
    public String showSummaryPage() {
        return "attendance/attendance-summary";
    }

    // ✅ 근태 항목 페이지
    @GetMapping("/items")
    public String showItemsPage() {
        return "attendance/attendance-items";
    }

    // ✅ 휴가 내역 페이지
    @GetMapping("/leave")
    public String showLeavePage() {
        return "attendance/attendance-leave";
    }

    // ✅ 출근 처리
    @PostMapping("/check-in")
    @ResponseBody
    public Map<String, Object> checkIn(@RequestParam("empId") String empId) {
        Map<String, Object> response = new HashMap<>();
        try {
            AttendanceCheckDTO result = attendanceService.checkIn(empId);
            response.put("checkInTime", result.getCheckInTime());
            response.put("message", "출근 완료!");
        } catch (Exception e) {
            response.put("message", "출근 처리에 실패했습니다.");
        }
        return response;
    }

    // ✅ 퇴근 처리
    @PostMapping("/check-out")
    @ResponseBody
    public Map<String, Object> checkOut(@RequestParam("empId") String empId) {
        Map<String, Object> response = new HashMap<>();
        try {
            AttendanceCheckDTO result = attendanceService.checkOut(empId);
            response.put("checkOutTime", result.getCheckOutTime());
            response.put("message", "퇴근 완료!");
        } catch (Exception e) {
            response.put("message", "퇴근 처리에 실패했습니다.");
        }
        return response;
    }

    // ✅ 출퇴근 기록 조회
    @PostMapping("/check-attendance")
    @ResponseBody
    public Map<String, Object> checkAttendance(
        @RequestParam("empId") String empId,
        @RequestParam("workDate") String workDateStr) {

        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate workDate = LocalDate.parse(workDateStr);
            AttendanceCheckDTO result = attendanceService.getAttendanceByEmpIdAndDate(empId, workDate);
            if (result != null) {
                response.put("attendance", result);
                response.put("lateStatus", result.getLateStatus());
                response.put("message", "출퇴근 기록 조회 성공");
            } else {
                response.put("message", "해당 날짜의 출퇴근 기록이 없습니다.");
            }
        } catch (Exception e) {
            response.put("message", "출퇴근 기록 조회에 실패했습니다.");
        }
        return response;
    }

    // ✅ 지각 현황 조회
    @PostMapping("/late-status")
    @ResponseBody
    public Map<String, Object> getLateStatus(
        @RequestParam("empId") String empId,
        @RequestParam("startDate") String startDateStr,
        @RequestParam("endDate") String endDateStr) {

        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("empId", empId);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);

            List<AttendanceLateDTO> lateDetails = attendanceService.getLateDetailsByEmpIdAndDateRange(paramMap);
            AttendanceLateDTO lateStats = attendanceService.getLateStatsByEmpIdAndDateRange(paramMap);

            response.put("lateDetails", lateDetails);
            response.put("lateStats", lateStats);
            response.put("message", "지각 현황 조회 성공");
        } catch (Exception e) {
            response.put("message", "지각 현황 조회에 실패했습니다.");
        }
        return response;
    }

    // ✅ 근무 통계 조회
    @PostMapping("/work-summary")
    @ResponseBody
    public Map<String, Object> getWorkSummary(
        @RequestParam("empId") String empId,
        @RequestParam("startDate") String startDateStr,
        @RequestParam("endDate") String endDateStr) {

        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("empId", empId);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);

            AttendanceWorkCheckDTO result = attendanceService.findWorkSummaryByEmpIdAndDateRange(paramMap);
            response.put("workSummary", result);
            response.put("message", "근무 통계 조회 성공");
        } catch (Exception e) {
            response.put("message", "근무 통계 조회에 실패했습니다.");
        }
        return response;
    }

    // ✅ 근태 항목 조회
    @PostMapping("/work-item")
    @ResponseBody
    public AttendanceWorkListDTO getWorkItemByDate(@RequestBody AttendanceWorkListDTO requestDto) {
        return attendanceService.findWorkItemByDateAndCategory(requestDto);
    }

    // ✅ 휴가 조건 검색
    @PostMapping("/leave/search")
    public String searchLeave(
        @RequestParam("empId") String empId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        Model model) {

        List<AttendanceLeaveDTO> leaveList;

        if (date != null) {
            leaveList = attendanceService.findLeaveByDate(empId, date);
        } else if (startDate != null && endDate != null) {
            leaveList = attendanceService.findLeaveByDateRange(empId, startDate, endDate);
        } else {
            model.addAttribute("alertMsg", "조회 조건을 다시 확인해주세요.");
            return "attendance/attendance-leave";
        }

        if (leaveList.isEmpty()) {
            model.addAttribute("alertMsg", "해당하는 정보가 존재하지 않습니다.");
        } else {
            model.addAttribute("leaveList", leaveList);
        }

        return "attendance/attendance-leave";
    }

    // ✅ 휴가 상세 보기
    @GetMapping("/leave/report/{leaveId}")
    public String viewLeaveReport(@PathVariable("leaveId") String leaveId, Model model) {
        AttendanceLeaveDTO report = attendanceService.findLeaveReportById(leaveId);
        model.addAttribute("report", report);
        return "attendance/attendance-leave-report-detail";
    }
}