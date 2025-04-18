package com.itwill.persistence;

import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.itwill.domain.UserSessionVO;

@Repository
public class UserSessionDAOImpl implements UserSessionDAO {
	
	private static final String NAMESPACE = "com.itwill.mapper.UserSessionMapper";

    @Inject
    private SqlSession sqlSession;
    
    @Override
    public UserSessionVO findByEmpId(String empId) {
        return sqlSession.selectOne(NAMESPACE + ".findByEmpId", empId);
    }

	@Override
	public void USInsert(UserSessionVO session) {
		sqlSession.insert(NAMESPACE + ".insert", session);
		
	}

	@Override
	public void USUpdate(UserSessionVO session) {
		sqlSession.update(NAMESPACE + ".update", session);
		
	}

	@Override
	public void insertSession(UserSessionVO newSession) {
		
		sqlSession.insert(NAMESPACE + ".insertIfNotExists", newSession);
	}
	
	
	
}
