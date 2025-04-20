package com.itwill.attendance.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class WorkTypeAdminDTO {
    private String empId;          // 사원 ID
    private String empName;        // 사원 이름
    private String departmentName; // 부서명
    private LocalDate workDate;    // 근무 날짜
    private String workType;       // 근무 형태 (정상근무, 지각, 결근, 휴가 등)
    private String reason;         // 사유 (휴가 종류, 결근 사유 등)
}
