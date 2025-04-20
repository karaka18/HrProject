package com.itwill.employee.service;

import java.util.List;
import com.itwill.employee.domain.ResignationVO;

public interface ResignationService {
    void insertResignation(ResignationVO vo);
    List<ResignationVO> getAllResignations();
    ResignationVO getResignationById(int resignId);
    void approveResignation(int resignId, String approver);
    void rejectResignation(int resignId, String approver);
    void updateStatus(int resignId, String status, String approver);
}
