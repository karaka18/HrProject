package com.itwill.persistence;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.domain.UserSessionVO;

public interface LoginHistoryDAO {
	
	// 로그인 로그 기록
    public LoginHistoryVO insertLoginHistory(LoginHistoryVO history);
    
    // 로그인 실패 기록
    public int countRecentFailedLogins(String empId);

	public boolean isAccountLocked(String empId);

	public LoginHistoryVO findLatestByEmpId(String empId);
    
}