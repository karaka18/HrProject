package com.itwill.attendance.service;

import com.itwill.attendance.dto.LeaveStatusDTO;
import com.itwill.attendance.mapper.LeaveMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveMapper leaveMapper;

    @Override
    public LeaveStatusDTO getLeaveStatusByEmpId(String empId) {
        return leaveMapper.selectLeaveStatusByEmpId(empId);
    }

    @Override
    public List<LeaveStatusDTO> getAllLeaveStatuses() {
        return leaveMapper.selectAllLeaveStatuses();
    }
}
