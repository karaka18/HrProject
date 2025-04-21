package com.itwill.attendance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class WorkRecordDTO {
    private String empId; // 사원 ID
    private LocalDateTime checkInTime; // 출근 시간
    private LocalDateTime checkOutTime; // 퇴근 시간
    private long workMinutes; // 근무 시간 (분 단위)

   
}
