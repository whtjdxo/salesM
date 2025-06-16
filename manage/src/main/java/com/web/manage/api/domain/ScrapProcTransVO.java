package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapProcTransVO {
    private String  chainNo;            // 가맹점 코드
    private String  vanCd;              // Scrap VAN, 배달대행 사
    private String  userId;             // 사용자ID
    private int     resultCode;         // 결과코드
    private String  resultMsg;          // 결과메시지
}
