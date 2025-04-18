package com.itwill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.domain.MemberVO;
import com.itwill.service.MemberService;

@Controller
public class AdminEmployeeController {

	@Autowired
    private MemberService memberService;
	
	@PostMapping("/add")
	public String addEmployee(@RequestParam("empId") String empId, RedirectAttributes rttr) {
		MemberVO member = new MemberVO();
	    member.setEmpId(empId);
	    member.setEmpPw(new BCryptPasswordEncoder().encode("1234"));

	    try {
	    	memberService.insertMinimalMember(member);
	        rttr.addFlashAttribute("message", "사원이 성공적으로 추가되었습니다.");
	    } catch (Exception e) {
	        rttr.addFlashAttribute("error", "사원 추가 중 오류 발생.");
	    }

	    return "redirect:/admin/employee/info";
	}
	
}
