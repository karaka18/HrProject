package com.itwill.attendance.service;

import java.util.List;

import com.itwill.attendance.dto.LeaveHistoryDTO;

public interface LeaveService {

    List<LeaveHistoryDTO> getLeaveHistory(String empId, String startDate, String endDate);

    int getRemainingLeaveDays(String empId);
    
    
}
