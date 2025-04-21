package com.itwill.attendance.mapper;

import com.itwill.attendance.dto.LeaveStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LeaveMapper {
    LeaveStatusDTO selectLeaveStatusByEmpId(String empId);
    List<LeaveStatusDTO> selectAllLeaveStatuses();
}
