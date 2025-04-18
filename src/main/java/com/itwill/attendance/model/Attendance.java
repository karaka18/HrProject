package com.itwill.attendance.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    private LocalDateTime date;          // 출근일자 (DB에서 저장되는 날짜)
    private String attendanceId;         // 출퇴근 기록 ID (DB에서 저장되는 값)
    private String employeeId;           // 사원 ID (DB에서 저장되는 값)
    private String status;               // 출근/지각/결근 등 상태 (DB에서 저장되지 않을 수 있음)
    private String lateReason;           // 지각 사유 (DB에서 저장되지 않을 수 있음)
    private String workType;             // 근무 형태 (DB에서 저장되지 않을 수 있음)
    
    // 출퇴근 시간 필드만 남김
    private LocalDateTime checkIn;       // 출근 시간
    private LocalDateTime checkOut;      // 퇴근 시간

    // 날짜 포맷을 지정할 때 사용
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 상태와 근무 형태를 동적으로 생성하는 로직 예시
    public void setWorkStatus() {
        if (this.checkIn != null && this.checkOut != null) {
            // 출근 상태 확인
            if (checkIn.isAfter(date.withHour(9).withMinute(0).withSecond(0))) {
                this.status = "지각";
            } else {
                this.status = "출근";
            }

            // 퇴근 시간이 존재하면 근무 형태를 '근무완료'로 설정
            if (checkOut.isAfter(checkIn)) {
                this.workType = "근무완료";
            }
        }
    }

    // 출근 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 반환
    public String getFormattedCheckIn() {
        return checkIn != null ? checkIn.format(DATE_FORMATTER) : "N/A";
    }

    // 퇴근 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 반환
    public String getFormattedCheckOut() {
        return checkOut != null ? checkOut.format(DATE_FORMATTER) : "N/A";
    }

    // 예시: 상태가 지각인 경우 지각 사유를 생성하는 로직
    public void generateLateReason() {
        if (this.status != null && this.status.equals("지각")) {
            this.lateReason = "교통사고"; // 예시로, 지각 사유를 고정값으로 설정 (실제 시스템에서는 더 복잡할 수 있음)
        }
    }

    // 출퇴근 상태와 근무 형태를 처리하는 메서드
    public void processAttendance() {
        setWorkStatus();  // 출근 상태와 근무 형태 설정
        generateLateReason();  // 지각 사유 생성
    }
}
