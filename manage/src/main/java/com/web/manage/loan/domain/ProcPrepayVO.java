package com.web.manage.loan.domain;

import lombok.Data;

@Data
public class ProcPrepayVO {
    private String  prepay_loan_no; 
    private String  prepay_recv_date;
    private String  prepay_memo;
    private String  prepay_user_id;
    private int     resultCode;
    private String  resultMsg;
}