package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapUserVO {
    private String userId;
    private String userPwd;
    private String userAuthKey;
}
