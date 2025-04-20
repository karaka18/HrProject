package com.itwill.employee.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwill.employee.domain.EmployeeVO;

public interface EmployeeDAO {
	List<EmployeeVO> getAllEmployees();		// ��� ���� ��ȸ
    EmployeeVO getEmployeeById(String empId);	// Ư�� ���� ��ȸ
    void updateEmployeeUser(EmployeeVO employee);
    void updateEmployeeAdmin(EmployeeVO employee);
    int updateResignationDate(@Param("empId") String empId, @Param("empQd") Date empQd);
    void deleteEmployee(String empId);
    
    
}

