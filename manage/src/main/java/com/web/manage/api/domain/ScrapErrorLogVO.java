package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapErrorLogVO {
    private String userId;
    private String apiAuthKey;
    private String authKey;
    private String vanCd;
    private String chainNo;
    private String chainNm;
    private String loginId;
    private String loginPwd;
    private String errorMsg; 
}
