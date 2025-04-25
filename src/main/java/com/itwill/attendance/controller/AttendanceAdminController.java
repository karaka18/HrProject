package com.itwill.attendance.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itwill.attendance.dto.*;
import com.itwill.attendance.service.AttendanceAdminService;

@Controller
@RequestMapping("/admin/attendance")
public class AttendanceAdminController {

    @Autowired
    private AttendanceAdminService attendanceAdminService;

    // ✅ 1. 전체 출퇴근 기록 조회
    @GetMapping("/list")
    public String getAdminAttendanceList(
        @RequestParam(value = "empName", required = false) String empName,
        @RequestParam(value = "workDate", required = false) Date workDate,
        Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("empName", empName);
        params.put("workDate", workDate);

        List<AttendanceAdminCheckDTO> attendanceList = attendanceAdminService.getAdminAttendanceList(params);

        for (AttendanceAdminCheckDTO dto : attendanceList) {
            if (dto.getCheckInTime() != null && dto.getCheckInTime().toLocalDateTime().getHour() >= 9) {
                dto.setIsLate(true);
                dto.setWorkStatus("지각");
            } else if (dto.getCheckInTime() != null && dto.getCheckOutTime() != null) {
                dto.setWorkStatus("정상 출근");
            } else if (dto.getCheckInTime() != null) {
                dto.setWorkStatus("출근");
            } else {
                dto.setWorkStatus("미출근");
            }
        }

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("empName", empName);
        model.addAttribute("workDate", workDate);
        return "attendance/admin_attendance";
    }

    // ✅ 2. 휴가 내역 조회
    @GetMapping("/leave-list")
    public String getLeaveList(
        @RequestParam(required = false) String empId,
        @RequestParam(required = false) String empName,
        Model model,
        HttpServletRequest request) {

        List<AttendanceAdminLeaveDTO> leaveList = attendanceAdminService.getLeaveListByAdmin(empId, empName);

        if (leaveList == null || leaveList.isEmpty()) {
            request.setAttribute("message", "해당하는 정보가 존재하지 않습니다.");
            return "attendance/admin_leave_check";
        }

        model.addAttribute("leaveList", leaveList);
        return "attendance/admin_leave_check";
    }

    // ✅ 3. 지각 현황 조회
    @GetMapping("/late-list")
    public String getAdminLateStatusList(
        @RequestParam(value = "empName", required = false) String empName,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate,
        Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("empName", empName);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<AttendanceAdminLateDTO> lateList = attendanceAdminService.getLateStatusByAdmin(params);
        model.addAttribute("lateList", lateList);

        return "attendance/admin_lateness_check";
    }

    // ✅ 4. 근무 관리 리스트
    @GetMapping("/work-list")
    public String showWorkList(Model model) {
        List<AttendanceAdminWorkDTO> workList = attendanceAdminService.getWorkStatusByAdmin(new HashMap<>());
        model.addAttribute("workList", workList);
        return "attendance/admin_work_list";
    }

    // ✅ 근무 등록 폼 진입 (추가됨!)
    @GetMapping("/work-insert-form")
    public String showWorkInsertForm() {
        return "attendance/admin_work_insert_form";
    }

    // ✅ 근무 등록 처리
    @PostMapping("/work-insert")
    public String insertWorkStatus(@RequestParam Map<String, String> params, Model model) {
        boolean success = attendanceAdminService.insertWorkStatus(params);
        if (success) {
            model.addAttribute("successMessage", "등록이 완료되었습니다.");
        } else {
            model.addAttribute("errorMessage", "등록에 실패했습니다.");
        }
        return "redirect:/admin/attendance/work-list"; // ✅ 수정됨
    }

    // ✅ 근무 수정 폼 진입
    @GetMapping("/work-update-form/{empId}/{workDate}")
    public String showWorkUpdateForm(
        @PathVariable String empId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date workDate,
        Model model) {

        AttendanceAdminUpdateAndDeleteDTO dto = attendanceAdminService.getWorkDetail(empId, workDate);
        model.addAttribute("work", dto);
        return "attendance/admin_work_update_form";
    }

    // ✅ 근무 수정 처리
    @PostMapping("/work-update")
    public String updateWorkStatus(@ModelAttribute AttendanceAdminUpdateAndDeleteDTO dto, Model model) {
        boolean success = attendanceAdminService.updateWorkStatus(dto);
        if (success) {
            model.addAttribute("successMessage", "수정이 완료되었습니다.");
        } else {
            model.addAttribute("errorMessage", "수정에 실패했습니다.");
        }
        return "redirect:/admin/attendance/work-list"; // ✅ 수정됨
    }

    // ✅ 근무 삭제 처리
    @PostMapping("/work-delete")
    public String deleteWorkStatus(@ModelAttribute AttendanceAdminUpdateAndDeleteDTO dto, Model model) {
        boolean success = attendanceAdminService.deleteWorkStatus(dto);
        if (success) {
            model.addAttribute("successMessage", "삭제가 완료되었습니다.");
        } else {
            model.addAttribute("errorMessage", "삭제에 실패했습니다.");
        }
        return "redirect:/admin/attendance/work-list"; // ✅ 수정됨
    }
}