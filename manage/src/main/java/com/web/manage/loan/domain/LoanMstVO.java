package com.web.manage.loan.domain;

import lombok.Data;

@Data
public class LoanMstVO {
    private String loan_no;
    private String corp_cd;
    private String chain_no;
    private String loan_status;
    private String loan_type;
    private String sub_code;
    private String cont_dt;
    private String princ_amt;
    private String int_rate;
    private String loan_day;
    private String loan_edt;    
    private String int_amt;
    private String tot_loan_amt;
    private String loan_expr_dt;
    private String delay_rate;
    private String loan_sdt;
    private String delay_yn;
    private String delay_cnt;
    private String pre_loan_amt;
    private String pre_pay_amt;
    private String use_yn;
    private String loan_memo;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;
}
