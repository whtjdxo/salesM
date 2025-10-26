package com.web.manage.deposit.domain;

import java.util.List;

import lombok.Data;

@Data
public class DepositExcelDataVO {
    private String corp_tp;
    private String corp_cd;
    private String bank_cd;
    private String ent_user_id;
    private List<DepositExcelRowDataVO> excelData;

}
