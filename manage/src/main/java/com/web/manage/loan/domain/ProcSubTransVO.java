package com.web.manage.loan.domain;

import lombok.Data;

@Data
public class ProcSubTransVO {
    private String  loan_no; 
    private String  sc_seq;
    private String  user_id;
    private int     resultCode;
    private String  resultMsg;

}
