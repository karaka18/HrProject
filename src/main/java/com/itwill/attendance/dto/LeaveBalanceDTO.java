package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [6. 사용자 휴가 잔여 일수 확인 DTO]
 * - 사용자의 연차, 사용한 연차 및 잔여 연차 일수를 포함하는 DTO
 * - 사용자가 현재 남은 휴가 일수 및 사용한 연차 정보를 조회할 때 사용
 */
@Data
@Builder
public class LeaveBalanceDTO {

    private String empId;             // 사원 ID
    private String empName;           // 사원 이름
    private int totalLeaveDays;       // 총 연차 일수
    private int usedLeaveDays;        // 사용한 연차 일수
    private int remainingLeaveDays;   // 잔여 연차 일수
}
