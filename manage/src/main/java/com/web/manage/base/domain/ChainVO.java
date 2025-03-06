package com.web.manage.base.domain;

import lombok.Data;

@Data
public class ChainVO {
    private String chain_no;
    private String chain_name;
    private String biz_no;
    private String chain_type;
    private String chain_type_nm;
    private String bank_cd;
    private String bank_cd_nm;
    private String account_no;
    private String account_pwd;
    private String web_id;
    private String web_pwd;
    private String api_url;
    private String memo;
    private String use_yn;
    private String ent_user_id;
    private String ent_dttm;
    private String upt_user_id;
    private String upt_dttm;
}
