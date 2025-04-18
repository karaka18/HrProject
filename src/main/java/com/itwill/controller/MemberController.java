package com.itwill.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.domain.MemberVO;
import com.itwill.service.LoginHistoryService;
import com.itwill.service.MemberService;
import com.itwill.util.PasswordEncoderUtil;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Inject
	private MemberService mService;

	@Inject
	private LoginHistoryService lService;
	

	// 로그인
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String memberLoginGET() {
		logger.info(" /login -> memberLoginGET()호출");

		logger.info(" /member/login.jsp 페이지로 이동");
		return "/member/login"; // "/login"
	}

	// HTTP 상태 405 – 허용되지 않는 메소드
	// Request method 'POST' not supported
	// => GET방식의 주소는 있지만 POST방식의 주소 호출이 없는경우 에러

	// ~~ /member/login
	// [ body ]
	// userid = "....."
	// userpw = "....."

	// 로그인 POST
	@PostMapping(value = "/login")
	public String memberLoginPOST( /* @ModelAttribute */ MemberVO vo, HttpSession session, RedirectAttributes rttr,
			HttpServletRequest request
	/*
	 * @ModelAttribute("userid") String userid,
	 * 
	 * @RequestParam("userpw") String userpw
	 */) {
		logger.info("  memberLoginPOST() 호출 ");
		// 한글처리 인코딩 => 생략(web.xml-필터사용)
		// 전달된 정보 저장(id,pw)
		// logger.info(" userid : "+userid );
		// logger.info(" userpw : "+userpw );

		String clientIp = request.getRemoteAddr();
		String inputId = vo.getEmpId();
		logger.info(" 로그인 vo : " + vo);

		// IPv6 로컬 주소일 경우 IPv4로 변환
		if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
			clientIp = "127.0.0.1";
		}

		// DB에 존재하는 ID인지 확인
		MemberVO existVO = mService.getMemberById(inputId);
		if (existVO == null) {
			logger.info("해당 emp_id가 DB에 존재하지 않습니다. emp_id: " + inputId);
			rttr.addFlashAttribute("message", "존재하지 않는 계정입니다.");
			return "redirect:/member/login";
		}

		// 📌 로그인 잠금 여부 확인!
		boolean isLocked = lService.isAccountLocked(inputId);
		if (isLocked) {
			rttr.addFlashAttribute("message", "계정이 잠겨 있습니다.");
			return "redirect:/member/login";
		}

		// 서비스에 로그인체크 동작을 호출해서 확인
		// MemberVO resultVO = mService.memberLoginCheck(vo);
		
//		// 로그인 실패
//		if (resultVO == null) {
//			logger.info(" 로그인 실패! ");
		
		MemberVO resultVO = mService.getMemberById(inputId);

		logger.info(" 결과 : {}", resultVO);
		
		// 비밀번호 비교 후 로그인 실패 체크
		if (resultVO == null || !PasswordEncoderUtil.matches(vo.getEmpPw(), resultVO.getEmpPw())) {
		    logger.info(" 로그인 실패! ");

			// 최근 실패 횟수 확인
			int failCount = lService.countRecentFailedLogins(inputId) + 1;
			logger.info("최근 실패 횟수: {}", failCount);

			// 5회 이상 실패시 처리
			if (failCount >= 5) {
				logger.info("계정 잠금 처리!");

				// 상태를 LOCKED로 기록
				LoginHistoryVO historylock = new LoginHistoryVO();
				historylock.setEmpId(inputId);
				historylock.setLoginIp(clientIp);
				historylock.setLoginStatus("LOCKED"); // 👈 요거 추가
				historylock.setLoginResult("FAIL"); // 실패지만 잠금임
				lService.insertLoginHistory(historylock); // 다시 기록!

				rttr.addFlashAttribute("message", "계정이 잠금되었습니다. 관리자에게 문의하세요.");
				return "redirect:/member/login";
			} else {
				// 실패 시 기록
				LoginHistoryVO historyfail = new LoginHistoryVO();
				historyfail.setEmpId(inputId);
				historyfail.setLoginIp(clientIp);
				historyfail.setLoginStatus("INACTIVE");
				historyfail.setLoginResult("FAIL");
				lService.insertLoginHistory(historyfail);
				
				lService.initSessionIfNotExists(inputId, failCount);
				
				rttr.addFlashAttribute("message", "로그인 실패! (" + failCount + "회 실패)[5회 실패시 잠금]");
			}

			return "redirect:/member/login";
		}

		// 로그인 성공 시 기록
		LoginHistoryVO historysuc = new LoginHistoryVO();
		historysuc.setEmpId(inputId);
		historysuc.setLoginIp(clientIp);
		historysuc.setLoginStatus("ACTIVE");
		historysuc.setLoginResult("SUCCESS");
		lService.insertLoginHistory(historysuc);
		
		lService.upsertUserSessionToActive(inputId);

		// 세션 영역에 로그인 성공한 사용자의 아이디를 저장
		session.setAttribute("id", resultVO.getEmpId());
		
		// 로그인 성공 시 세션에 role_id 저장
		session.setAttribute("role_id", resultVO.getRoleId());

//		if ("1234".equals(resultVO.getEmpPw())) {
		
		if (PasswordEncoderUtil.matches("1234", resultVO.getEmpPw())) {
			session.setAttribute("loginUser", resultVO.getEmpId()); // 세션에 사용자 정보 저장
			return "redirect:/member/userinfo"; // 특정 페이지로 이동
		} else {
			rttr.addFlashAttribute("message", "정상 로그인 되었습니다.");
			return "redirect:/member/main"; // 메인 페이지로 이동
		}

	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String findMemberInfoGET() {
		logger.info(" /find -> findMemberInfoGET()호출");

		logger.info(" /find.jsp 페이지로 이동");
		return "/member/find";
	}

	@RequestMapping(value = "/find/email", method = RequestMethod.GET)
	public String findMemberEmailGET() {
		logger.info(" /findEmail -> findMemberEmailGET()호출");

		logger.info(" /findEmail.jsp 페이지로 이동");
		return "/member/findEmail";
	}

	@PostMapping(value = "/find/email")
	public String findMemberEmailPOST(MemberVO vo) {

		return "/member/login"; // "/login"
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String MemberMainGET(HttpSession session, Model model) {
		logger.info(" /member/main -> MemberMainGET()호출");
		
		// 세션에서 role_id 값을 가져옴
	    Object roleId = session.getAttribute("role_id");
		
	    // role_id가 없을 경우 기본값 설정 (예: 직원)
	    if (roleId == null) {
	        roleId = 4; // 기본적으로 직원 역할로 설정
	    }

	    // role_id 값을 모델에 추가하여 JSP에서 사용
	    model.addAttribute("role_id", roleId);

	    logger.info(" /member/main.jsp 페이지로 이동");
	    return "/member/main";
	}

	@PostMapping(value = "/main")
	public String memberMainPOST(MemberVO vo) {
		return "/member/main";
	}

	@RequestMapping(value = "/userinfo", method = RequestMethod.GET)
	public String MemberUserinfoGET() {
		logger.info(" /member/main -> MemberUserinfoGET()호출");

		logger.info(" /member/userinfo.jsp 페이지로 이동");
		return "/member/userinfo";
	}

	@PostMapping(value = "/userinfo")
	public String memberUserinfoPOST(MemberVO vo) {
		return "/member/userinfo";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // 세션 초기화
	    return "redirect:/member/login"; // 로그인 페이지로 이동
	}

}
