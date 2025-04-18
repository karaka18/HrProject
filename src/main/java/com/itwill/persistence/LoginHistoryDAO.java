package com.itwill.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.itwill.domain.LoginHistoryVO;

@Mapper
public interface LoginHistoryDAO {
	
	// 로그인 로그 기록
    public LoginHistoryVO insertLoginHistory(LoginHistoryVO history);
    
    // 로그인 실패 기록
    public int countRecentFailedLogins(String empId);

	public boolean isAccountLocked(String empId);
    
}