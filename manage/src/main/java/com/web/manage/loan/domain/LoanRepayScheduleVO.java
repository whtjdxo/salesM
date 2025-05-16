package com.web.manage.loan.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class LoanRepayScheduleVO {
    private String  loan_no;
    private String  sc_seq;
    private String  sc_date;
    private String  repay_tot_amt;
    private String  repay_princ_amt;
    private String  repay_int_amt;
    private String  balance_amt;

    private String  recv_tot_amt;
    private String  recv_princ_amt;
    private String  recv_int_amt;

    private String  remain_tot_amt;
    private String  remain_princ_amt;
    private String  remain_int_amt;

    private String  recv_yn;
    private String  recv_dt;
    private String  sub_trans_yn;
    private String  sub_strans_dttm;
    private String  sub_trans_no;

    private String  ent_dttm;
    private String  ent_user_id;
    private String  upt_dttm;
    private String  upt_user_id;
    
}
