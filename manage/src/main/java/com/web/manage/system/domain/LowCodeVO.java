package com.web.manage.system.domain;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LowCodeVO implements Serializable{

	private static final long serialVersionUID = -604097480737736952L;

	@NotBlank(message="코드를 입력해주십시오.")
	private String low_code;
	@NotBlank(message="코드명을 입력해주십시오.")
	private String low_code_nm;
	@NotBlank(message="노출여부 선택해주십시오.")
	private String low_view_yn;
	@NotBlank(message="사용여부 선택해주십시오.")
	private String low_use_yn;
	@NotBlank(message="순번을 입력해주십시오.")
	private String low_sq;
	private String chk_cd1;
	private String chk_cd2;
	private String low_rmks;
	@NotBlank(message="그룹코드를 선택해주십시오.")
	private String low_gr_code;
	@NotBlank(message="분류명을 선택해주십시오.")
	private String low_gr_nm;
	private String user_id;


}
