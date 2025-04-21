package com.itwill.attendance.controller;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // 출근/퇴근 상세 조회
    @GetMapping("/attendance/detail")
    public String getDailyAttendance(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     Model model) {
        // AttendanceDetailDTO를 가져와서 바로 사용
        AttendanceDetailDTO detail = attendanceService.getAttendanceDetailDTO(empId, date);
        model.addAttribute("attendanceDetail", detail);
        return "attendance/attendanceDetail";
    }

    // 지각 출석 조회
    @GetMapping("/attendance/late")
    public String getLateAttendances(@RequestParam String empId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                     Model model) {
        List<LatenessAdminDTO> latenessList = attendanceService.getLateAttendanceList(empId, start, end);
        model.addAttribute("lateAttendances", latenessList);
        return "attendance/lateAttendanceList";
    }

    // 근무 기록 조회
    @GetMapping("/attendance/work-records")
    public String getWorkRecords(@RequestParam String empId,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                  Model model) {
        List<AttendanceDetailDTO> attendanceDetails = attendanceService.getAttendanceDetails(empId, start, end);

        // AttendanceDetailDTO의 Time -> LocalDateTime으로 변환 필요
        List<WorkRecordDTO> workList = attendanceDetails.stream()
                .map(att -> {
                    LocalDateTime checkIn = null;
                    LocalDateTime checkOut = null;
                    long minutes = 0;

                    if (att.getCheckInTime() != null) {
                        checkIn = LocalDateTime.of(att.getDate().toLocalDate(), att.getCheckInTime().toLocalTime());
                    }
                    if (att.getCheckOutTime() != null) {
                        checkOut = LocalDateTime.of(att.getDate().toLocalDate(), att.getCheckOutTime().toLocalTime());
                    }

                    if (checkIn != null && checkOut != null) {
                        minutes = java.time.Duration.between(checkIn, checkOut).toMinutes();
                    }

                    return new WorkRecordDTO(att.getEmpId(), checkIn, checkOut, minutes);
                })
                .collect(Collectors.toList());

        long totalMinutes = workList.stream().mapToLong(WorkRecordDTO::getWorkMinutes).sum();
        int totalDays = workList.size();

        model.addAttribute("workList", workList);
        model.addAttribute("totalMinutes", totalMinutes);
        model.addAttribute("totalDays", totalDays);

        return "attendance/workRecordsList";
    }

    // 출결 상태 조회
    @GetMapping("/attendance/status")
    public String getAttendanceStatus(@RequestParam String empId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                      Model model) {
        List<AttendanceStatusDTO> statusList = attendanceService.getAttendanceStatus(empId, start, end);
        model.addAttribute("statusList", statusList);
        return "attendance/statusList";
    }

    // 휴가 내역 조회
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

    // 관리자 근무 입력
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
