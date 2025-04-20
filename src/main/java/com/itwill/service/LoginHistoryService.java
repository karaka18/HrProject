package com.itwill.service;

import com.itwill.domain.LoginHistoryVO;

public interface LoginHistoryService {

	// 로그인 로그 기록 동작
	public LoginHistoryVO insertLoginHistory(LoginHistoryVO history);
	
	// 로그인 실패 횟수 기록 동작
	public int countRecentFailedLogins(String empId);

	// 로그인 상태 LOCKED인지 확인
	public boolean isAccountLocked(String empId);
	
	// 로그인 상태 ACTIVE로 변경
	public void upsertUserSessionToActive(String empId);

	// 이메일 인증후 비밀번호 변경시 로그인 로직때문에 history에 success강제 삽입
	public void insertFakeSuccessLogin(String empId, String string);

	public void initSessionIfNotExists(String inputId, int failCount);
	
	public LoginHistoryVO getLastLoginByEmpId(String empId);
	
}
