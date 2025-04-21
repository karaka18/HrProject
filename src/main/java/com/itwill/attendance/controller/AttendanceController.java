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

    @GetMapping("/attendance/detail")
    public String getDailyAttendance(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     Model model) {
        AttendanceDetailDTO detail = attendanceService.getDailyAttendance(empId, date);
        model.addAttribute("attendanceDetail", detail);
        return "attendance/attendanceDetail";
    }

    @GetMapping("/attendance/late")
    public String getLateAttendances(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                     Model model) {
        List<LatenessAdminDTO> latenessList = attendanceService.getLateAttendanceList(empId, start, end);
        model.addAttribute("lateAttendances", latenessList);
        return "attendance/lateAttendanceList";
    }

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

    @GetMapping("/attendance/status")
    public String getAttendanceStatus(@RequestParam String empId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                      Model model) {
        List<AttendanceStatusDTO> statusList = attendanceService.getAttendanceStatus(empId, start, end);
        model.addAttribute("statusList", statusList);
        return "attendance/statusList";
    }

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
    
    @PostMapping("/workinput/admin")
    public String adminWorkInput(@RequestParam("empId") String empId,
                                 @RequestParam("checkInTime") String checkInTime,
                                 @RequestParam("checkOutTime") String checkOutTime,
                                 @RequestParam("workType") String workType,
                                 @RequestParam("absenceReason") String absenceReason,
                                 Model model) {
        // WorkInputDTO 객체 생성
        WorkInputDTO workInputDTO = WorkInputDTO.builder()
                .empId(empId)
                .checkInTime(LocalDateTime.parse(checkInTime))
                .checkOutTime(LocalDateTime.parse(checkOutTime))
                .workType(workType)
                .absenceReason(absenceReason)
                .build();

        // AttendanceService에 데이터를 전달하여 작업 수행
        boolean isInserted = attendanceService.insert(workInputDTO); // insert 메서드 호출
        
        if (isInserted) {
            model.addAttribute("message", "근무 정보가 성공적으로 입력되었습니다.");
        } else {
            model.addAttribute("message", "근무 정보 입력에 실패했습니다.");
        }

        return "attendance/workInputResult"; // 결과 페이지로 이동
    }
}
