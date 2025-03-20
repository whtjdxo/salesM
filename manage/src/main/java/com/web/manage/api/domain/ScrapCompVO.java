package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapCompVO {
    private String vanCd;
    private String chainNo;
    private String userId;
    private String bankAccountNo;
    private String bankSearchPwd;
    private String bankSearchId;
    private String apiAuthKey;
    private String authKey;
}
