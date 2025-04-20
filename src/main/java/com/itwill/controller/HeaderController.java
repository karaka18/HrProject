package com.itwill.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwill.domain.LoginHistoryVO;
import com.itwill.service.LoginHistoryService;
import com.itwill.util.ResponseAPI;

@Controller
@RequestMapping("/common")
public class HeaderController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(HeaderController.class);
	
	@Autowired
    private LoginHistoryService loginHistoryService;

	@GetMapping("/time")
	@ResponseBody
	public ResponseAPI verifyCode(HttpSession session) {
        String empId = (String) session.getAttribute("id");
        logger.info(empId);
        if (empId == null) return null;

        ResponseAPI result = new ResponseAPI();
        
        
        LoginHistoryVO lastLogin = loginHistoryService.getLastLoginByEmpId(empId);
        logger.info(lastLogin.getLoginTime().toString());
        if (lastLogin != null && lastLogin.getLoginTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 타임존 설정
            
            String lastTime = sdf.format(lastLogin.getLoginTime()).toString();
            Map<String, Object> resultMap = new HashMap<>();
            
            resultMap.put("status", "SUCCESS");
            resultMap.put("time", lastTime);
            result.setResult(resultMap);
            
            return result;
            //return sdf.format(lastLogin.getLoginTime());
        }
        return null;
    }
	
}
