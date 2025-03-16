package com.web.manage.base.domain;
 

import lombok.Data;

@Data
public class ChainVanVO {
    private String van_seq ;
    private String chain_no;
    private String van_chain_no;
    private String van_cd;
    private String van_id;
    private String van_pwd;
    private String term_id;
    private String van_use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id; 
}
