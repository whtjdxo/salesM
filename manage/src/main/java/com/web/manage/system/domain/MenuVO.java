package com.web.manage.system.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Alias("menuVo")
@Data
public class MenuVO implements Serializable{
	
	private static final long serialVersionUID = -5859886073472413513L;
	
	
	/*@NotNull(message="상위메뉴를 선택해주세요")
	private String menu;*/
	@NotBlank(message="메뉴ID를 입력해주세요")
	private String menu_cd;
	@NotBlank(message="메뉴명을 입력해주세요")
	private String menu_nm;
	private String user_id;
	@NotBlank(message="메뉴유형을 선택해주세요")
	private String menu_tp;
	@NotBlank(message="URL를 입력해주세요")
	private String menu_url;
	@NotBlank(message="정렬순서를 입력해주세요")
	private String menu_ord;
	private String site_gb_cd;
	private String menu_prnt_cd;
	private String upt_dttm;
	private String ent_dttm;
	private String use_yn;
	private String view_yn;
	private String upt_user_id;
	private String old_menu_cd;
	private String menu_use_yn;
	private String view_icon;
	
}

