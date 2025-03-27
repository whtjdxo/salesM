package com.web.manage.trans.domain;

import lombok.Data;

@Data
public class MapCodeVO {
    private String map_seq;
    private String map_pcode;
    private String map_code_nm;
    private String map_code;
    private String map_text;
    private String use_yn;
    private String ent_user_id;
    private String ent_dttm;
    private String upt_user_id;
    private String upt_dttm;
}
