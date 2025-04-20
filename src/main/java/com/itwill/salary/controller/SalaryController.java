package com.itwill.salary.controller;

import com.itwill.salary.dto.SalaryDetailDTO;
import com.itwill.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    // ✅ 사용자용 급여명세서 페이지
    @GetMapping("")
    public String showUserSalaryPage() {
        return "salary/salary-detail"; // user-sidebar.jsp 포함
    }

    // ✅ 관리자용(복사본) 급여명세서 페이지
    @GetMapping("/a")
    public String showAdminSalaryPage() {
        return "salary/salary-detail-a"; // admin-sidebar.jsp 포함
    }

    // ✅ 공통: 급여 명세서 조회 (AJAX)
    @GetMapping("/view")
    @ResponseBody
    public SalaryDetailDTO viewSalary(@RequestParam("salMonth") String salMonth,
                                      HttpSession session) {

        String empId = (String) session.getAttribute("id");
        if (empId == null) {
            throw new RuntimeException("로그인 정보 없음");
        }
        return salaryService.getSalaryDetail(empId, salMonth);
    }

    // ✅ 공통: 엑셀 다운로드
    @GetMapping("/download/excel")
    public void downloadSalaryExcel(@RequestParam("salMonth") String salMonth,
                                    HttpServletResponse response,
                                    HttpSession session) throws IOException {

        String empId = (String) session.getAttribute("id");
        if (empId == null) {
            throw new RuntimeException("로그인 필요");
        }

        SalaryDetailDTO dto = salaryService.getSalaryDetail(empId, salMonth);

        String filename = URLEncoder.encode("급여명세서_" + salMonth + ".xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);

        salaryService.writeSalaryExcel(dto, response.getOutputStream());
    }

    // ✅ 공통: PDF 다운로드
    @GetMapping("/download/pdf")
    public void downloadSalaryPdf(@RequestParam("salMonth") String salMonth,
                                  HttpServletResponse response,
                                  HttpSession session) throws IOException {

        String empId = (String) session.getAttribute("id");
        if (empId == null) {
            throw new RuntimeException("로그인 필요");
        }

        SalaryDetailDTO dto = salaryService.getSalaryDetail(empId, salMonth);

        String filename = URLEncoder.encode("급여명세서_" + salMonth + ".pdf", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);

        salaryService.writeSalaryPdf(dto, response.getOutputStream());
    }
}