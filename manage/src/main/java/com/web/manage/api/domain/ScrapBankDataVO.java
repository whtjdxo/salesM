package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapBankDataVO {
    private String bankCd;
	private String chainNo;
	private String accountNo;
	private String tradeDttm;
	private String tradeGb;
	private String remark;
	private String outAmt;            	
	private String inAmt;
	private String remainAmt;            	
	private String centerNm;
    private String entDttm;
}
