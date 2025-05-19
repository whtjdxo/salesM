package com.web.manage.loan.domain;

import lombok.Data;

@Data
public class ProcPrepayVO {
    private String  loanNo;
    private String  recvDate;
    private String  memo;
    private String  userId;
    private int     resultCode;
    private String  resultMsg;
}