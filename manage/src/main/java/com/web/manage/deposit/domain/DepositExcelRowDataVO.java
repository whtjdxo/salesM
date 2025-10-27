package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class DepositExcelRowDataVO { 
    private String corpType;
    private String corpCd;    
    private String bankCd;
    private String accountNo;
    private String tradeDttm;
    private String tradeGb;
    private String remark;
    private String outAmt;
    private String inAmt;
    private String remainAmt;
    private String centerNm;
    private String entUserId;
}
