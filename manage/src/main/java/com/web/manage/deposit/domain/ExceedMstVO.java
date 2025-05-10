package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class ExceedMstVO {
    private String exc_no;
    private String exc_status;
    private String chain_no;
    private String exc_date;
    private String exc_type;
    private String exc_code;    
    private String exc_memo;    
    private String occur_amt; 
    private String issue_amt; 
    private String remain_amt; 
    private String adjust_no;
    private String remit_no;
    private String remit_dt;
    private String use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;

}
