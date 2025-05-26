package com.web.manage.common.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("sessionVo")
@Data
public class SessionVO implements Serializable{

	private static final long serialVersionUID = 6301450078844122257L;
	private String userId;
	private String userPwd;
	private String userNm;
	private String groupCode;
	private List<String> userGroup;
	private String userGrpCd;
	private String authGrpCd;
	private String userCorpGb;
	private String userCorpCd;
	private String userCorpNm;
	private String userCorpType;
	private String userHomeUrl;	

	private List<HashMap<String, Object>>  menu;
	private List<HashMap<String, Object>>  menu2nd;
	private List<HashMap<String, Object>>  menu3rd;


}
