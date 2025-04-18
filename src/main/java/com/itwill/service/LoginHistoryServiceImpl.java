package com.itwill.service;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.persistence.LoginHistoryDAO;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

	
	@Inject
	@Qualifier("loginHistoryDAO")
	private LoginHistoryDAO LHdao;
	
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
	
}
