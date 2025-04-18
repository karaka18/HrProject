package com.itwill.persistence;

import org.springframework.data.repository.query.Param;

import com.itwill.domain.UserSessionVO;

public interface UserSessionDAO {
	
	public UserSessionVO findByEmpId(String empId);

	// 계정 세션 테이블 데이터 없을시 추가
    public void USInsert(UserSessionVO session);
    
    // 계정 세션 테이블 상태 변경 LOCKED -> ACTIVE
    public void USUpdate(UserSessionVO session);

	public void insertSession(UserSessionVO newSession);
    
    
	
}
