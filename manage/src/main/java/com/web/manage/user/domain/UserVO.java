package com.web.manage.user.domain;

import lombok.Data;

@Data
public class UserVO {
    private String user_id;
    private String corp_cd;
    private String corp_type;
    private String user_nm;
    private String user_pwd;
    private String user_gb;
    private String auth_grp_cd;
    private String hp_no;
    private String tel_no;
    private String email;
    private String zip_no;
    private String addr;
    private String addr_dtl;
    private String use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;
 
}
