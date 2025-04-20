package com.itwill.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.domain.UserSessionVO;
import com.itwill.persistence.LoginHistoryDAO;
import com.itwill.persistence.UserSessionDAO;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

	
	@Inject
	private LoginHistoryDAO LHdao;
	
	@Inject
	private UserSessionDAO userSessionDAO;
	
	@Override
	public LoginHistoryVO insertLoginHistory(LoginHistoryVO history) {
		LHdao.insertLoginHistory(history); // DB에 insert
	    return history; // auto_increment id나 시간 포함돼 있겠지
	}
	
	@Override
	public int countRecentFailedLogins(String empId) {
	    return LHdao.countRecentFailedLogins(empId);
	}
	
	@Override
	public boolean isAccountLocked(String empId) {
		return LHdao.isAccountLocked(empId);
	}

	@Override
	public void upsertUserSessionToActive(String empId) {
		UserSessionVO session = userSessionDAO.findByEmpId(empId);
	    if (session != null) {
	        session.setAccountStatus("ACTIVE");
	        session.setUpdatedAt(LocalDateTime.now());
	        userSessionDAO.USUpdate(session);
	    } else {
	        UserSessionVO newSession = new UserSessionVO();
	        newSession.setEmpId(empId);
	        newSession.setAccountStatus("ACTIVE");
	        newSession.setCreatedAt(LocalDateTime.now());
	        newSession.setUpdatedAt(LocalDateTime.now());
	        userSessionDAO.USInsert(newSession);
	    }
		
	}

	@Override
	public void insertFakeSuccessLogin(String empId, String loginIp) {
		// 1. 로그인 히스토리에 강제 성공 기록 삽입
	    LoginHistoryVO fakeLogin = new LoginHistoryVO();
	    fakeLogin.setEmpId(empId);
	    fakeLogin.setLoginIp(loginIp);
	    fakeLogin.setLoginStatus("ACTIVE");
	    fakeLogin.setLoginResult("SUCCESS");

	    LHdao.insertLoginHistory(fakeLogin);
	    
	    upsertUserSessionToActive(empId);
		
		
	}

	@Override
	public void initSessionIfNotExists(String empId, int failCount) {
		UserSessionVO session = userSessionDAO.findByEmpId(empId);
	    if (session == null) {
	        UserSessionVO newSession = new UserSessionVO();
	        newSession.setEmpId(empId);
	        newSession.setAccountStatus("INACTIVE");
	        newSession.setLoginAttempts(failCount);
	        userSessionDAO.insertSession(newSession);
	    }
		
	}

	@Override
	public LoginHistoryVO getLastLoginByEmpId(String empId) {
		 return LHdao.findLatestByEmpId(empId);
	}
	
	
	
	
	
}
