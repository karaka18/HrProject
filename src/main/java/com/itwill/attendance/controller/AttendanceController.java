package com.itwill.attendance.controller;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.service.AttendanceService;
import com.itwill.attendance.service.LeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private LeaveService leaveService;

    // 1. 사용자 메인 페이지 (출근 정보 + 이름 표시)
    @RequestMapping("attendance/attendance_main")
    public String showMainPage(HttpSession session, Model model) {
       
    	//로그인한 사용자 정보 가져오기 
    	String empId = (String) session.getAttribute("id");
        if (empId == null) {
            return "redirect:/member/login"; //미로그인 시 로그인 페이지로 이동
        }

        String empName = attendanceService.getEmployeeNameById(empId); // 별도 구현 필요
        session.setAttribute("empName", empName);
        
        //해당 직원 출근 기록 조회
        AttendanceDetailDTO attendanceDetail = attendanceService.getAttendanceDetailDTO(empId, LocalDate.now());
        model.addAttribute("attendanceDetail", attendanceDetail);

        return "attendance/attendance_main";
    }

    
    // 2. 사용자 근태 항목 조회
    @GetMapping("attendance/attendace_items")
	public String getAttendanceDetail(@RequestParam String empId,
	            					@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	            					Model model) {
	AttendanceDetailDTO detail = attendanceService.getAttendanceDetailDTO(empId, date);
	model.addAttribute("attendanceDetail", detail);
       
		return "attendance/attendance_items";
    }

    
    
    
    // 3. 사용자 지각 현황
    @GetMapping("attendance/attendance_late")
    public String getLatenessList(@RequestParam String empId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model) {
	
	List<LateAttendanceDTO> LateList = attendanceService.getLateAttendanceList(empId, start, end);
	
	model.addAttribute("LateList", LateList);
	
	// JSP 페이지로 이동
	return "attendance/attendance_late";
	}
    

    // 4. 사용자 근무 조회
    @GetMapping("attendance/attendance_summary")
    public String getMyWorkRecords(@RequestParam String empId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<AttendanceSummaryDTO> records = attendanceService.getWorkRecords(empId, startDate, endDate); // 🔁 타입 맞춤
        model.addAttribute("records", records);
        
        //JSP 페이지로 이동
        return "attendance/attendance_summary";
    }


    // 5. 휴가 내역 조회
    @GetMapping("attendance/attendance-leave")
    public String getLeaveHistory(
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        HttpSession session,
        Model model
    ) {
        // 로그인된 사원 ID 가져오기
        String empId = (String) session.getAttribute("empId");

        List<LeaveHistoryDTO> leaveList = leaveService.getLeaveHistory(empId, startDate, endDate);
        model.addAttribute("leaveList", leaveList);
        
        return "attendance/attendance_leave"; // JSP 경로
    }

    // 5-1. 남은 연차 조회
    @GetMapping("/attendance/attendance-leave")
    public String showLeaveForm(HttpSession session, Model model) {
        String empId = (String) session.getAttribute("empId");
        String empName = (String) session.getAttribute("empName");

        // 남은 연차 조회
        int remainingDays = leaveService.getRemainingLeaveDays(empId);

        model.addAttribute("empId", empId);
        model.addAttribute("empName", empName);
        model.addAttribute("remainingDays", remainingDays);

        return "attendance/attendance_leave";
    }

    
    
    
    
//    // 7. 관리자: 사원 출퇴근 기록 조회
//    @GetMapping("/admin/records")
//    public String getAdminRecords(@RequestParam String empId,
//                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//                                  Model model) {
//        List<AttendanceDetailDTO> details = attendanceService.getAttendanceDetails(empId, startDate, endDate);
//        model.addAttribute("details", details);
//        return "attendance/adminRecords";
//    }
//
//    // 8. 출결 상태 리스트
//    @GetMapping("/status")
//    public String getAttendanceStatus(@RequestParam String empId,
//                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//                                      Model model) {
//        List<AttendanceSummaryDTO> statusList = attendanceService.getAttendanceStatus(empId, startDate, endDate);
//        model.addAttribute("statusList", statusList);
//        return "attendance/status";
//    }
//
//    // 9. 관리자: 부서 전체 근무 형태 통계
//    @GetMapping("/admin/worktype")
//    public String getAdminWorkType(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//                                   Model model) {
//        List<WorkTypeAdminDTO> workTypes = attendanceService.getWorkTypeByPeriodForAdmin(startDate, endDate);
//        model.addAttribute("workTypes", workTypes);
//        return "attendance/worktype";
//    }
//
//    // 10. 관리자: 근무 입력
//    @PostMapping("/admin/input")
//    public String insertWorkRecord(@ModelAttribute WorkInputDTO inputDTO) {
//        attendanceService.insertWorkRecord(inputDTO);
//        return "redirect:/attendance/admin/records?empId=" + inputDTO.getEmpId();
//    }
//
//    // 11. 관리자: 근무 수정
//    @PostMapping("/admin/update")
//    public String updateWorkRecord(@ModelAttribute WorkInputDTO inputDTO) {
//        attendanceService.updateWorkRecord(inputDTO);
//        return "redirect:/attendance/admin/records?empId=" + inputDTO.getEmpId();
//    }

    
}
