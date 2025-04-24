package com.web.manage.base.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CorpVO {
    private String corp_cd;
    @NotNull(message="여신사 명을 입력해주십시오.")
    private String corp_nm;
    @NotNull(message="사업자번호를 입력해주십시오.")
    private String biz_no;
    @NotNull(message="여신사 유형을 선택해주십시오.")
    private String corp_type;
    private String corp_type_nm;
    private String bank_cd;
    private String bank_cd_nm;
    private String account_no;
    private String account_pwd;
    private String web_id;
    private String web_pwd;
    private String depositor;
    private String api_url;
    private String memo;
    private String use_yn;
    private String ent_user_id;
    private String ent_dttm;
    private String upt_user_id;
    private String upt_dttm;
    
}
