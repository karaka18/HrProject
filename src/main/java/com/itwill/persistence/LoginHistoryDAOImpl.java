package com.itwill.persistence;

import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.domain.UserSessionVO;

@Repository
public class LoginHistoryDAOImpl implements LoginHistoryDAO {

    @Inject
    private SqlSession sqlSession;

    private static final String namespace = "com.itwillbs.mapper.LoginHistoryMapper"; // XML 안 바꿨으면 그대로

    @Override
    public LoginHistoryVO insertLoginHistory(LoginHistoryVO history) {
        sqlSession.insert(namespace + ".insertLoginHistory", history);
        return history;
    }
    
    @Override
    public int countRecentFailedLogins(String empId) {
        return sqlSession.selectOne(namespace + ".countRecentFailedLogins", empId);
    }
    
    @Override
    public boolean isAccountLocked(String empId) {
    	String status = sqlSession.selectOne(namespace + ".checkAccountLocked", empId);
        return "LOCKED".equals(status);
    }
    
    
    
}