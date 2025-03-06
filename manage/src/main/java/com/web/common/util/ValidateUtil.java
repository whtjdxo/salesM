package com.web.common.util;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.web.manage.common.domain.ReturnDataVO;


public class ValidateUtil {
	
	public static ReturnDataVO validCheck(BindingResult bindResult, ReturnDataVO vo){
		
		if (bindResult.hasErrors()) {
            // 에러 출력
            List<ObjectError> list = bindResult.getAllErrors();
            for (ObjectError e : list) {
            	if("NotEmpty".equals(e.getCode())){
            		vo.setResultMsg(e.getDefaultMessage()+"를(을) 입력해주세요.");
            	} else {
            		vo.setResultMsg(e.getDefaultMessage());
            	}
            }
            vo.setResultCode("V999");
        } else {
        	vo.setResultCode("");
        }
		return vo;
	}
}
