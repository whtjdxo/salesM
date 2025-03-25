package com.web.manage.trans.domain;

import lombok.Data;

@Data
public class TransProcessVO {
    private String chainNo;
    private String workDate;
    private String vanCode;
    private String userId;
    private int    resultCode   ;
    private String resultMsg;

}
