package com.itwill.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserSessionVO {
	
	private String empId;
    private String accountStatus;
    private LocalDateTime lastLoginTime;
    private int loginAttempts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	
	
}
