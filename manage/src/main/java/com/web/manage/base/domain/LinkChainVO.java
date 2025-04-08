package com.web.manage.base.domain;

import lombok.Data;

@Data
public class LinkChainVO {
    
    private String link_org_chain_no;        // 체인번호
    private String link_chain_no;       // 링크체인번호
    private String link_memo;           // 링크메모
    private String link_use_yn;        // 링크사용여부

    // private String link_ent_dttm;       // 등록일자
    private String ent_user_id;    // 등록자ID
    // private String link_upt_dttm;       // 수정일자
    private String upt_user_id;    // 수정자ID    

}
