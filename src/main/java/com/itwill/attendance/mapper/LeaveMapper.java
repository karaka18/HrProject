package com.itwill.attendance.mapper;

import java.util.List;
import java.util.Map;
import com.itwill.attendance.dto.LeaveHistoryDTO;

public interface LeaveMapper {
    
    // 사용자 ID와 기간을 기준으로 휴가 내역 조회
    List<LeaveHistoryDTO> selectLeaveHistoryByDate(Map<String, Object> paramMap);


    // 특정 사원의 휴가 내역 조회
    List<LeaveHistoryDTO> getLeaveHistory(String empId, String startDate, String endDate);

    // 사원의 총 부여된 연차 일수 조회
    Integer getTotalGrantedLeave(String empId);

    // 사원의 사용한 연차 일수 조회
    Integer getUsedLeave(String empId);


}
