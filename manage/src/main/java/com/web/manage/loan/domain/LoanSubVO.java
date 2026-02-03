package com.web.manage.loan.domain;

import lombok.Data;

@Data
public class LoanSubVO {
    private String sub_id;
    private String chain_no;
    private String sub_type;
    private String sub_code;
    private String sub_status;
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
    private String reg_date;
    private String use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;
}
