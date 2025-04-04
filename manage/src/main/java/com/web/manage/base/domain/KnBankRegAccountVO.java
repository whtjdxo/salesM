package com.web.manage.base.domain;

public class KnBankRegAccountVO {
    private String cmRecordGb;            //  C - 9    아래 참고
    private String cmCompCd;            //  C - 8    은행 부여 업체코드
    private String cmBankCd2;            //  C - 2    미사용
    private String cmMsgCode;            //  C - 4    
    private String cmWorkGb;            //  C - 3    
    private String cmSendCnt;            //  C - 1    고정값 ‘1’
    private String cmTransNo;            //  N - 6    
    private String cmTransDt;            //  D - 8    YYYYMMDD
    private String cmTransTm;            //  T - 6    HHMMSS
    private String cmRetrunCd;            //  C - 4    
    private String cmBankReturnCd;            //  C - 4    은행 응답코드
    private String cmSearchDt;            //  D - 8    처리결과 조회시
    private String cmSearchNo;            //  N - 6    
    private String cmBankTransNo;            //  C - 15    
    private String cmBankCd3;            //  C - 3    은행코드
    private String cmBufferField;            //  C - 13    
    private String recordGb;            //  C - 1    ‘D’
    private String seq;            //  N - 7    ‘0000000’
    private String bankCd2;            //  C - 2    
    private String accounNo;            //  C - 16    
    private String reqGb;            //  C - 1    등록:1, 
    private String autoPayDt;            //  D - 2    
    private String branchCd6;            //  C - 6    
    private String reqDt;            //  D - 8    YYYYMMDD
    private String result;            //  C - 1    정상:Y, 불능:N
    private String resultCd;            //  C - 4    
    private String idChk;            //  C - 1    Y
    private String idNo;            //  C - 13    
    private String payerNo;            //  C - 20    
    private String corpUseInof;            //  C - 16    
    private String orgCd;            //  C - 10    
    private String bankCd3;            //  C - 3    
    private String branchCd7;            //  C - 7    
    private String useOrganCd;            //  C - 20    
    private String depositorGb;            //  C - 1    
    private String agreeFileGb;            //  C - 1    
    private String fileExtNm;            //  C - 4    
    private String fileSize;            //  N - 7    
    private String buffer;            //  C - 49    

    
    
    public String getCmRecordGb() {
        return cmRecordGb;
    }

    public void setCmRecordGb(String cmRecordGb) {
        this.cmRecordGb = cmRecordGb;
    }

    public String getCmCompCd() {
        return cmCompCd;
    }

    public void setCmCompCd(String cmCompCd) {
        this.cmCompCd = cmCompCd;
    }

    public String getCmBankCd2() {
        return cmBankCd2;
    }

    public void setCmBankCd2(String cmBankCd2) {
        this.cmBankCd2 = cmBankCd2;
    }

    public String getCmMsgCode() {
        return cmMsgCode;
    }

    public void setCmMsgCode(String cmMsgCode) {
        this.cmMsgCode = cmMsgCode;
    }

    public String getCmWorkGb() {
        return cmWorkGb;
    }

    public void setCmWorkGb(String cmWorkGb) {
        this.cmWorkGb = cmWorkGb;
    }

    public String getCmSendCnt() {
        return cmSendCnt;
    }

    public void setCmSendCnt(String cmSendCnt) {
        this.cmSendCnt = cmSendCnt;
    }

    public String getCmTransNo() {
        return cmTransNo;
    }

    public void setCmTransNo(String cmTransNo) {
        this.cmTransNo = cmTransNo;
    }

    public String getCmTransDt() {
        return cmTransDt;
    }

    public void setCmTransDt(String cmTransDt) {
        this.cmTransDt = cmTransDt;
    }

    public String getCmTransTm() {
        return cmTransTm;
    }

    public void setCmTransTm(String cmTransTm) {
        this.cmTransTm = cmTransTm;
    }

    public String getCmRetrunCd() {
        return cmRetrunCd;
    }

    public void setCmRetrunCd(String cmRetrunCd) {
        this.cmRetrunCd = cmRetrunCd;
    }

    public String getCmBankReturnCd() {
        return cmBankReturnCd;
    }

    public void setCmBankReturnCd(String cmBankReturnCd) {
        this.cmBankReturnCd = cmBankReturnCd;
    }

    public String getCmSearchDt() {
        return cmSearchDt;
    }

    public void setCmSearchDt(String cmSearchDt) {
        this.cmSearchDt = cmSearchDt;
    }

    public String getCmSearchNo() {
        return cmSearchNo;
    }

    public void setCmSearchNo(String cmSearchNo) {
        this.cmSearchNo = cmSearchNo;
    }

    public String getCmBankTransNo() {
        return cmBankTransNo;
    }

    public void setCmBankTransNo(String cmBankTransNo) {
        this.cmBankTransNo = cmBankTransNo;
    }

    public String getCmBankCd3() {
        return cmBankCd3;
    }

    public void setCmBankCd3(String cmBankCd3) {
        this.cmBankCd3 = cmBankCd3;
    }

    public String getCmBufferField() {
        return cmBufferField;
    }

    public void setCmBufferField(String cmBufferField) {
        this.cmBufferField = cmBufferField;
    }

    public String getRecordGb() {
        return recordGb;
    }

    public void setRecordGb(String recordGb) {
        this.recordGb = recordGb;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getBankCd2() {
        return bankCd2;
    }

    public void setBankCd2(String bankCd2) {
        this.bankCd2 = bankCd2;
    }

    public String getAccounNo() {
        return accounNo;
    }

    public void setAccounNo(String accounNo) {
        this.accounNo = accounNo;
    }

    public String getReqGb() {
        return reqGb;
    }

    public void setReqGb(String reqGb) {
        this.reqGb = reqGb;
    }

    public String getAutoPayDt() {
        return autoPayDt;
    }

    public void setAutoPayDt(String autoPayDt) {
        this.autoPayDt = autoPayDt;
    }

    public String getBranchCd6() {
        return branchCd6;
    }

    public void setBranchCd6(String branchCd6) {
        this.branchCd6 = branchCd6;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultCd() {
        return resultCd;
    }

    public void setResultCd(String resultCd) {
        this.resultCd = resultCd;
    }

    public String getIdChk() {
        return idChk;
    }

    public void setIdChk(String idChk) {
        this.idChk = idChk;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPayerNo() {
        return payerNo;
    }

    public void setPayerNo(String payerNo) {
        this.payerNo = payerNo;
    }

    public String getCorpUseInof() {
        return corpUseInof;
    }

    public void setCorpUseInof(String corpUseInof) {
        this.corpUseInof = corpUseInof;
    }

    public String getOrgCd() {
        return orgCd;
    }

    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }

    public String getBankCd3() {
        return bankCd3;
    }

    public void setBankCd3(String bankCd3) {
        this.bankCd3 = bankCd3;
    }

    public String getBranchCd7() {
        return branchCd7;
    }

    public void setBranchCd7(String branchCd7) {
        this.branchCd7 = branchCd7;
    }

    public String getUseOrganCd() {
        return useOrganCd;
    }

    public void setUseOrganCd(String useOrganCd) {
        this.useOrganCd = useOrganCd;
    }

    public String getDepositorGb() {
        return depositorGb;
    }

    public void setDepositorGb(String depositorGb) {
        this.depositorGb = depositorGb;
    }

    public String getAgreeFileGb() {
        return agreeFileGb;
    }

    public void setAgreeFileGb(String agreeFileGb) {
        this.agreeFileGb = agreeFileGb;
    }

    public String getFileExtNm() {
        return fileExtNm;
    }

    public void setFileExtNm(String fileExtNm) {
        this.fileExtNm = fileExtNm;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

}
