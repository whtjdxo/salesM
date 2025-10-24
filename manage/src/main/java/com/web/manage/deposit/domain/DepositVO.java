package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class DepositVO {
    private String deposit_no;
    private String chain_no;
    private String bank_cd;
    private String gubun;    
    private String bank_in_date;    
    private String bank_in_time;  
    private String card_acq;
    private String in_amt;
    private String remark;
    private String credit_depo_chk;
    private String ent_user_id;
    
}
