package com.itwill.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itwill.attendance.mapper.LeaveMapper;
import com.itwill.attendance.dto.LeaveHistoryDTO;

@Service
public class LeaveServiceImpl implements LeaveService {

	 @Autowired
	    private LeaveMapper leaveMapper;

	    @Override
	    public List<LeaveHistoryDTO> getLeaveHistory(String empId, String startDate, String endDate) {
	        // 해당 메서드 구현
	        return leaveMapper.getLeaveHistory(empId, startDate, endDate);
	    }

	    @Override
	    public int getRemainingLeaveDays(String empId) {
	        Integer total = leaveMapper.getTotalGrantedLeave(empId);   // 예: 15
	        Integer used = leaveMapper.getUsedLeave(empId);            // 예: 7

	        return (total != null ? total : 0) - (used != null ? used : 0);
	    }
    
}
