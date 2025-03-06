package com.web.manage.system.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SchoolMstVO implements Serializable{

	private static final long serialVersionUID = 3869070098907804093L;
	
	private String sch_cd;
	private String sch_nm;
	private String sch_full_nm;
	private String sch_grade_cd;
	private String sch_grade_nm;
	private String addr_road;
	private String sch_edu_mgt_cd;
	private String sch_edu_mgt_nm;
	private String ent_dttm;
	private String upt_dttm;
	
}