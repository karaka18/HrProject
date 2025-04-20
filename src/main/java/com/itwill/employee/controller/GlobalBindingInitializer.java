package com.itwill.employee.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@ControllerAdvice
public class GlobalBindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Timestamp용 바인더
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    try {
                        setValue(new Timestamp(tsFormat.parse(text).getTime()));
                    } catch (ParseException e) {
                        setValue(null);
                    }
                }
            }
        });

        // java.sql.Date용 바인더
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);  // 빈 값은 null 처리
                } else {
                    try {
                        setValue(new Date(dateFormat.parse(text).getTime()));
                    } catch (ParseException e) {
                        setValue(null); // 파싱 오류 시도 null 처리
                    }
                }
            }
        });
    }
}
