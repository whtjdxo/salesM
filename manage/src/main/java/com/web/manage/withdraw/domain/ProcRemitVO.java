package com.web.manage.withdraw.domain;

import lombok.Data;

@Data
public class ProcRemitVO {
    private String chainNo;
    private String wdNo;
    private String workDate;
    private String wdStatus;
    private String userId;
    private int    resultCode;
    private String resultMsg;
}
