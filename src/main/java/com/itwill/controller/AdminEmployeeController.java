package com.itwill.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.domain.MemberVO;
import com.itwill.service.MemberService;

@Controller
public class AdminEmployeeController {

	@Autowired
    private MemberService memberService;
	
	@PostMapping("/add")
	@ResponseBody
	public Map<String, Object> addEmployee(@RequestParam("empId") String empId) {
	    Map<String, Object> response = new HashMap<>();
	    
	    // 1. 먼저 사원번호가 이미 존재하는지 확인
	    MemberVO existingMember = memberService.getMemberById(empId);
	    if (existingMember != null) {
            response.put("success", false);
            response.put("message", "이미 존재하는 사원번호입니다.");
            return response; // 사원번호가 중복되면 바로 종료
        }

        // 2. 사원번호가 중복되지 않으면 사원 추가
        MemberVO member = new MemberVO();
        member.setEmpId(empId);
        member.setEmpPw(new BCryptPasswordEncoder().encode("1234"));

        try {
            memberService.insertMinimalMember(member);
            response.put("success", true);
            response.put("message", "사원이 성공적으로 추가되었습니다.");
            response.put("empId", empId.toString()); // 추가된 사원번호 보내기
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "사원 추가 중 오류가 발생했습니다.");
        }

        return response;
    }
	
}
