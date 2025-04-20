package com.itwill.employee.persistence;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.employee.domain.ResignationVO;

@Repository
public class ResignationDAOImpl implements ResignationDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "com.itwill.employee.persistence.ResignationDAO";

    @Override
    public void insertResignation(ResignationVO vo) {
        sqlSession.insert(NAMESPACE + ".insertResignation", vo);
    }

    @Override
    public List<ResignationVO> getAllResignations() {
        return sqlSession.selectList(NAMESPACE + ".getAllResignations");
    }

    @Override
    public ResignationVO getResignationById(int resignId) {
        return sqlSession.selectOne(NAMESPACE + ".getResignationById", resignId);
    }

    @Override
    public void approveResignation(int resignId, String approver) {
        sqlSession.update(NAMESPACE + ".approveResignation", 
            new java.util.HashMap<String, Object>() {{
                put("resignId", resignId);
                put("approver", approver);
            }});
    }

    @Override
    public void rejectResignation(int resignId, String approver) {
        sqlSession.update(NAMESPACE + ".rejectResignation", 
            new java.util.HashMap<String, Object>() {{
                put("resignId", resignId);
                put("approver", approver);
            }});
    }

    @Override
    public void updateStatus(int resignId, String status, String approver) {
        sqlSession.update(NAMESPACE + ".updateStatus", 
            new java.util.HashMap<String, Object>() {{
                put("resignId", resignId);
                put("status", status);
                put("approver", approver);
            }});
    }
}
