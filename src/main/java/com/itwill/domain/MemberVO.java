package com.itwill.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberVO {
	
	//@Getter
	private String empId; // emp_id -> empId
    private String empPw; // emp_pw -> empPw
    private String empName; // emp_name -> empName
    private String empEmail; // emp_email -> empEmail
    private String depId;	// dep_id -> depId;
    private Timestamp empRegistDate; // emp_registdate -> empRegistDate
    private Timestamp empModifyDate; // emp_modifydate -> empModifyDate
    private int roleId; // role_id -> roleId
	
	// alt shift s + r
	// alt shift s + s
	
}
