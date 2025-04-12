package com.web.manage.withdraw.domain;

import lombok.Data;

@Data
public class ProcSubReceiveVO {
    private String subNo;
    private String recvType;
    private String shiftChainNo;
    private String actionNo;
    private int     recvAmt;
    private String  memo;
    private String  userId;
    private int     resultCode;
    private int     resultBalanceAmt;
    private String  resultMsg;
}
