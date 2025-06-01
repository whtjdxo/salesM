package com.web.manage.withdraw.domain;

import lombok.Data;

@Data
public class SubMstVO {
    private String sub_no;
    private String sub_status;
    private String chain_no;
    private String reg_date;
    private String resv_date;
    private String sub_type;
    private String sub_code;    
    private String sub_memo;    
    private String occur_amt;
    private String occur_base_amt;
    private String occur_svc_amt;
    private String occur_crd_amt;
    private String recv_amt;
    private String recv_base_amt;
    private String recv_svc_amt;
    private String recv_crd_amt;
    private String remain_amt;
    private String remain_base_amt;
    private String remain_svc_amt;
    private String remain_crd_amt; 
    private String shift_flag;
    private String shift_sub_no;
    private String loan_no; 
    private String loan_sc_seq;
    private String remit_no;
    private String adjust_no;
    private String use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;

}
