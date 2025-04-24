package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class DepositVO {
    private String depositNo;
    private String depositDate;
    private String depositAmount;
    private String depositStatus;
    private String userId;
}