package com.web.manage.system.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupCodeVO implements Serializable{
	
	private static final long serialVersionUID = -5859886073472413513L;
	
	
	@NotNull(message="분류코드를 입력해주십시오.")
	private String clas_code;
	@NotNull(message="분류명을 입력해주십시오.")
	private String clas_nm;
	@NotNull(message="사용여부를 입력해주십시오.")
	private String clas_use_yn;
	private String user_id;
	
}
