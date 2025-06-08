package com.web.manage.deposit.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.deposit.domain.ProcDepositVO;
import com.web.manage.deposit.mapper.DepositMapper;

import ch.qos.logback.classic.Logger;

@Service
public class DepositService {

    @Autowired
    private DepositMapper depositMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    public int getQueryTotalCnt() {
        return depositMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositSummary(hashmapParam);
    }

    public HashMap<String, Object> getDepositSummaryTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositSummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoCardSummary(hashmapParam);
    }

    public HashMap<String, Object> getChainDepoStatus(HashMap<String, Object> hashmapParam) {
        return depositMapper.getChainDepoStatus(hashmapParam);
    }
    
    public List<HashMap<String, Object>> getDepoResvList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoResvList(hashmapParam);
    }

    //  transaction 처리 는 Procedure 에서 처리하도록 함
    public ReturnDataVO callProcChangeResvDate(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcChangeResvDate(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }
 
    public ReturnDataVO callProcDepositAdjust(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcDepositAdjust(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }

    public List<HashMap<String, Object>> getDepoAdjustSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustSummary(hashmapParam);
    }

    public HashMap<String, Object> getDepoAdjustSummTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustSummTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoAdjustCardSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustCardSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoAdjustList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustList(hashmapParam);
    }
    

    public ReturnDataVO callProcDepoAdjustCancel(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcDepoAdjustCancel(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }
}