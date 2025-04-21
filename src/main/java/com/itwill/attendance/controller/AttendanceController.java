package com.itwill.attendance.controller;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.service.AttendanceService;
import com.itwill.attendance.service.LeaveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final LeaveService leaveService;

    // 사용자 출퇴근 기록 상세
    @GetMapping("/attendance/detail")
    public String getDailyAttendance(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     Model model) {
        AttendanceDetailDTO detail = attendanceService.getDailyAttendance(empId, date);
        model.addAttribute("attendanceDetail", detail);
        return "attendance/attendanceDetail";
    }

    // 사용자 지각 현황
    @GetMapping("/attendance/late")
    public String getLateAttendances(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                     Model model) {
        List<LateAttendanceDTO> latenessList = attendanceService.getLateAttendanceList(empId, start, end);
        model.addAttribute("lateAttendances", latenessList);
        return "attendance/lateAttendanceList";
    }

    // 사용자 근무 조회
    @GetMapping("/attendance/work-records")
    public String getWorkRecords(@RequestParam String empId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                 Model model) {
        List<WorkRecordDTO> workList = attendanceService.getWorkRecords(empId, start, end);

        long totalMinutes = workList.stream().mapToLong(WorkRecordDTO::getWorkMinutes).sum();
        int totalDays = workList.size();

        model.addAttribute("workList", workList);
        model.addAttribute("totalMinutes", totalMinutes);
        model.addAttribute("totalDays", totalDays);

        return "attendance/workRecordsList";
    }

    // 사용자 근태 항목
    @GetMapping("/attendance/status")
    public String getAttendanceStatus(@RequestParam String empId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                      Model model) {
        List<AttendanceStatusDTO> statusList = attendanceService.getAttendanceStatus(empId, start, end);
        model.addAttribute("statusList", statusList);
        return "attendance/statusList";
    }

    // 사용자 휴가 내역 및 신청
    @GetMapping("/attendance/leave")
    public String getLeaveHistory(@RequestParam String empId,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                  Model model) {
        List<LeaveDTO> leaveHistory = attendanceService.getLeaveHistory(empId, start, end);
        int remainingLeave = attendanceService.getRemainingLeaveDays(empId);
        model.addAttribute("leaveHistory", leaveHistory);
        model.addAttribute("remainingLeave", remainingLeave);
        return "attendance/leaveHistory";
    }

    // 관리자 출퇴근 기록부 조회
    @GetMapping("/admin/attendance")
    public String getAllAttendanceDetails(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                          Model model) {
        List<AttendanceDetailDTO> attendanceDetails = attendanceService.getAttendanceDetails(startDate, endDate);
        model.addAttribute("attendanceDetails", attendanceDetails);
        return "admin/attendanceDetails";
    }

    @GetMapping("/admin/attendance/{empId}")
    public String getAttendanceDetail(@PathVariable String empId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      Model model) {
        AttendanceDetailDTO attendanceDetail = attendanceService.getAttendanceDetailByEmpIdAndDate(empId, date);
        model.addAttribute("attendanceDetail", attendanceDetail);
        return "admin/attendanceDetail";
    }

    // 관리자 휴가 일수 조회
    @GetMapping("/admin/leave/status/{empId}")
    public String getLeaveStatus(@PathVariable String empId, Model model) {
        LeaveStatusDTO leaveStatus = leaveService.getLeaveStatusByEmpId(empId);
        model.addAttribute("leaveStatus", leaveStatus);
        return "admin/leaveStatus";
    }

    @GetMapping("/admin/leave/status")
    public String getAllLeaveStatuses(Model model) {
        List<LeaveStatusDTO> leaveStatuses = leaveService.getAllLeaveStatuses();
        model.addAttribute("leaveStatuses", leaveStatuses);
        return "admin/leaveStatuses";
    }

    // 관리자 지각 현황
    @GetMapping("/lateness/admin")
    public String adminLateness(@RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate, Model model) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<LatenessAdminDTO> latenessList = attendanceService.getLatenessByPeriodForAdmin(start, end);
        model.addAttribute("latenessList", latenessList);
        return "lateness/admin";
    }

    // 관리자 근무 형태 현황
    @GetMapping("/worktype/admin")
    public String adminWorkType(@RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate, Model model) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<WorkTypeAdminDTO> workTypeList = attendanceService.getWorkTypeByPeriodForAdmin(start, end);
        model.addAttribute("workTypeList", workTypeList);
        return "worktype/admin";
    }

    // 관리자 근무 입력
    @PostMapping("/workinput/admin")
    public String adminWorkInput(@RequestParam("empId") String empId,
                                 @RequestParam("checkInTime") String checkInTime,
                                 @RequestParam("checkOutTime") String checkOutTime,
                                 @RequestParam("workType") String workType,
                                 @RequestParam("absenceReason") String absenceReason,
                                 Model model) {
        WorkInputDTO workInputDTO = WorkInputDTO.builder()
                .empId(empId)
                .checkInTime(LocalDateTime.parse(checkInTime))
                .checkOutTime(LocalDateTime.parse(checkOutTime))
                .workType(workType)
                .absenceReason(absenceReason)
                .build();
        
        //관리자 근무 입력 조회
        attendanceService.insertWorkRecord(workInputDTO);

        model.addAttribute("message", "근무 기록이 성공적으로 입력되었습니다.");
        return "redirect:/admin/workinput";
    }

}
