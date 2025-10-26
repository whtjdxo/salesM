package com.web.manage.deposit.domain;

import lombok.Data;

@Data

public class ProcTransDepositVO {
    private String  chainNo; 
    private String  userId;
    private int     resultCode;
    private String  resultMsg;

}
