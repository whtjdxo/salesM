package com.web.manage.common.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class ReturnDataVO implements Serializable{

	private static final long serialVersionUID = 1533024539259264563L;
	
	private String resultMsg;
	private String resultCode;
	private String returnVal;
	private Object data = null;
	
}