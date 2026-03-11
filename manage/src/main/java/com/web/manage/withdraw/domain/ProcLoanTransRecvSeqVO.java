package com.web.manage.withdraw.domain;
import lombok.Data;

@Data
public class ProcLoanTransRecvSeqVO {
    private String recvSeq;
    private String userId;
    private int    resultCode;
    private String resultMsg;
}
