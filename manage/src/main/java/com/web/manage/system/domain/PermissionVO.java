package com.web.manage.system.domain;

import java.io.Serializable;


import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
@Alias("permissionVO")
public class PermissionVO implements Serializable{
	
	private static final long serialVersionUID = -5859886073472413513L;

	@NotBlank(message="사이트구분을 선택해주세요.")
	@Length(min=1, max=50, message="사이트구분을 선택해주세요.")
	private String site_gb_cd;
	@NotBlank(message="사용자그룹코드를 선택해주세요.")
	@Length(min=1, max=50, message="사용자그룹코드를 선택해주세요.")
	private String auth_grp_cd;
	@NotBlank(message="메뉴코드를 선택해주세요.")
	@Length(min=1, max=100000, message="메뉴코드를 선택해주세요.")
	private String menu_cd;
	private String user_id;
	
}
