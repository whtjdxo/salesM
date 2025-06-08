package com.web.manage.deposit.domain;

import lombok.Data;

@Data
public class ProcDayCloseVO {
 
    private String  closeDate;        // 일마감일자
    private String  corpNo;         // 여신사번호
    private String  userId;          // 사용자ID
    private int     resultCode;      // 결과코드
    private String  resultMsg;       // 결과메시지
}
