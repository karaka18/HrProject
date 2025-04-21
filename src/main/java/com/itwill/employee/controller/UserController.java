package com.itwill.employee.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.employee.domain.AppointmentVO;
import com.itwill.employee.domain.DepartmentVO;
import com.itwill.employee.domain.EmployeeVO;
import com.itwill.employee.domain.NoticeVO;
import com.itwill.employee.domain.ResignationVO;
import com.itwill.employee.service.AppointmentService;
import com.itwill.employee.service.DepartmentService;
import com.itwill.employee.service.EmployeeService;
import com.itwill.employee.service.NoticeService;
import com.itwill.employee.service.ResignationService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private EmployeeService employeeService;
	
	@Autowired
    private NoticeService noticeService;
	
	@Autowired
    private AppointmentService appointmentService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private ResignationService resignationService;
	

	@GetMapping("/main")
	public String userMain(HttpSession session, Model model) {
	    String empId = (String) session.getAttribute("id");	// 로그인 하면 쓸거임
	    //String testEmpId = "240420001";
	    
	    if (empId == null) {
	        return "redirect:/member/login";
	    }
	    
	    
	    EmployeeVO employee = employeeService.getEmployeeById(empId); // 로그인 하면 쓸거임
	    //EmployeeVO employee = employeeService.getEmployeeById(testEmpId);
	    
	    System.out.println("조회된 사원: " + employee);
	    System.out.println("employee: " + employee);
	    System.out.println("입사일: " + employee.getEmpJd());
	  
	    
	    
	    // 입사일로부터 근무일 계산 (emp_jd 필드 활용)
	    if (employee.getEmpJd() != null) {
	        LocalDate joinDate = employee.getEmpJd().toLocalDate();
	        LocalDate today = LocalDate.now();
	        long workDays = ChronoUnit.DAYS.between(joinDate, today);
	        model.addAttribute("workDays", workDays);
	    } else {
	        model.addAttribute("workDays", "-");
	    }

	    model.addAttribute("employee", employee);

	    return "user/main";
	}

    
    // 인사관리 - 인사조회
    @GetMapping("/employee/info")
    public String employeeInfo(HttpSession session, Model model) {
        String empId = (String) session.getAttribute("id"); // 로그인 하면 쓸거임
        // String testEmpId = "240420001";
        EmployeeVO employee = employeeService.getEmployeeById(empId); // 로그인 하면 쓸거임
        // EmployeeVO employee = employeeService.getEmployeeById(testEmpId);
        model.addAttribute("employee", employee);
        return "user/employee/info";
    }
    
    // 정보 수정 페이지
    @GetMapping("/employee/edit")
    public String editEmployeeForm(HttpSession session, Model model) {
        String empId = (String) session.getAttribute("id"); //로그인 하면 쓸거임
        // String testEmpId = "240420001";
        EmployeeVO employee = employeeService.getEmployeeById(empId); //로그인 하면 쓸거임
        // EmployeeVO employee = employeeService.getEmployeeById(testEmpId);
        model.addAttribute("employee", employee);
        return "user/employee/edit";
    }
    
    @PostMapping("/employee/update")
    public String updateEmployee(EmployeeVO employee, HttpSession session, RedirectAttributes redirectAttributes) {
        // 실제 로그인 적용 시 아래 코드 사용
        String empId = (String) session.getAttribute("id");	// 로그인 하면 쓸거임

        // 테스트용 ID (개발 중에만 사용)
        // String empId = "240420001";

        employee.setEmpId(empId);
        employeeService.updateEmployeeUser(employee);
        redirectAttributes.addFlashAttribute("msg", "정보가 수정되었습니다.");

        return "redirect:/user/employee/info";
    }


    
    
    
    // 퇴사 신청 페이지
    @GetMapping("/employee/resignation")
    public String resignationForm(HttpSession session, Model model) {
        String empId = (String) session.getAttribute("id"); //로그인 하면 쓸거임
        //String testEmpId = "240420001";
        EmployeeVO employee = employeeService.getEmployeeById(empId); //로그인하면 쓸거임
        // EmployeeVO employee = employeeService.getEmployeeById(testEmpId);
        model.addAttribute("employee", employee);
        return "user/employee/resignation";
    }
    
    @PostMapping("/employee/resignation")
    public String processResignation(
            @RequestParam("resignationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date empQd,
            @RequestParam("resignationType") String resignationType,
            @RequestParam("resignationReason") String resignationReason,
            @RequestParam("handoverPlan") String handoverPlan,
            @RequestParam("contactAfter") String contactAfter,
            @RequestParam(value = "agreement", required = false) boolean agreement,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String empId = (String) session.getAttribute("id");
        // if (empId == null) empId = "240420001"; // 테스트용

        ResignationVO resignation = new ResignationVO();
        resignation.setEmpId(empId);
        resignation.setResignationDate(new java.sql.Date(empQd.getTime()));
        resignation.setResignationType(resignationType);
        resignation.setResignationReason(resignationReason);
        resignation.setHandoverPlan(handoverPlan);
        resignation.setContactAfter(contactAfter);
        resignation.setAgreement(agreement);
        resignation.setStatus("대기");

        try {
            resignationService.insertResignation(resignation);
            redirectAttributes.addFlashAttribute("msg", "퇴사 신청이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "퇴사 신청 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return "redirect:/user/employee/info";
    }




    
    
    
    // 발령 조회
    @GetMapping("/employee/appointment")
    public String appointmentInfo(HttpSession session, Model model) {
        String empId = (String) session.getAttribute("id");	// 로그인 하면 쓸거임
        // String testEmpId = "240420001";
        List<AppointmentVO> appointments = appointmentService.getAppointmentsByEmpId(empId);	// 로그인 하면 쓸거임
        //List<AppointmentVO> appointments = appointmentService.getAppointmentsByEmpId(testEmpId);
        model.addAttribute("appointments", appointments);
        return "user/employee/appointment";
    }
    
    @GetMapping("/employee/organization")
    public String organizationChart(Model model) {
        List<DepartmentVO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "user/employee/organization";
    }
    
    
    
    
    // 공지사항 목록
    @GetMapping("/notice/list")
    public String noticeList(Model model) {
        // 실제로는 서비스를 통해 공지사항 목록을 가져와 모델에 추가
    	List<NoticeVO> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "user/notice/list";
    }
    
    // 공지사항 상세
    @GetMapping("/notice/detail")
    public String noticeDetail(@RequestParam("not_id") int not_id, Model model) {
        noticeService.increaseViewCount(not_id); // 조회수 증가
        NoticeVO notice = noticeService.getNotice(not_id);
        model.addAttribute("notice", notice);
        return "user/notice/detail";
    }
    
    // 첨부파일 다운로드
    @GetMapping("/notice/download")
    public void downloadFile(@RequestParam("fileId") String fileId) {
        // 실제로는 파일 다운로드 로직 구현
    }
}