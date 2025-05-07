package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class ProcDepositVO {
    private String  resvDate;
    private String  depositDate;
    private String  adjustDate;
    private String  wdNo;
    private String  chainNo;
    private String  cardAcq;
    private String  changeResvDate;
    private String  adjustNo;
    private String  userId;
    private int     resultCode;
    private String  resultMsg;
}