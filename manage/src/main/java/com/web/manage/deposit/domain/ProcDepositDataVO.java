package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class ProcDepositDataVO {
    private String  depositNo;
    private String  userId;
    private int     resultCode;
    private String  resultMsg;
}
