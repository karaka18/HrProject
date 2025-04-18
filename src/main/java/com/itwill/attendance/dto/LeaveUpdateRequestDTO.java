package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;

/**
 * [8. 관리자용 사원별 휴가 관리 (휴가 추가 및 삭제) DTO]
 * - 관리자가 사원의 휴가 일수를 수정할 때 사용하는 DTO
 * - 휴가 일수 추가 또는 삭제 요청에 대한 정보를 포함
 */
@Data
@Builder
public class LeaveUpdateRequestDTO {

    private String empId;             // 사원 ID
    private int additionalLeaveDays;  // 추가/삭제할 연차 일수
    private String leaveReason;       // 연차 추가/삭제 사유
}
