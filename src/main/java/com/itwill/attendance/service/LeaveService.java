package com.itwill.attendance.service;

import com.itwill.attendance.dto.LeaveStatusDTO;
import java.util.List;

public interface LeaveService {
    LeaveStatusDTO getLeaveStatusByEmpId(String empId);
    List<LeaveStatusDTO> getAllLeaveStatuses();
}
