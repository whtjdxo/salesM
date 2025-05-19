package com.web.manage.withdraw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.withdraw.domain.ProcRemitVO;
import com.web.manage.withdraw.domain.SubMstVO;
import com.web.manage.withdraw.mapper.WithdrawMapper; 

import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.ReturnDataVO;

@Service
public class WithdrawService {
    @Autowired
    private WithdrawMapper withdrawMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return withdrawMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getWDSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDSummary(hashmapParam);
    }
    public HashMap<String, Object> getWDSummaryTotal(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDSummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getWDChainSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDChainSummary(hashmapParam);
    } 
    
    public List<HashMap<String, Object>> getWDCardSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDCardSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getWdResvlList(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDResvList(hashmapParam);
    }
 
 
    //  transaction 처리 는 Procedure 에서 처리하도록 함
    public ReturnDataVO callProcRemitMain (ProcRemitVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            // Van 스크래핑 데이터 이관 프로시저 호출
            withdrawMapper.callProcRemitMain(procVo);
            
            // 프로시저에서 설정한 OUT 파라미터 확인
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
    
    public boolean changeWorkDate(ProcRemitVO procVo) { 
        return withdrawMapper.changeWorkDate(procVo); 
    }
    public boolean changeWdStatus(ProcRemitVO procVo) {
        return withdrawMapper.changeWdStatus(procVo); 
    }


    public List<HashMap<String, Object>> getRemitSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getRemitSummary(hashmapParam);
    } 

    public HashMap<String, Object> getRemitSummaryTotal(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getRemitSummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getRemitList(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getRemitList(hashmapParam);
    }

    public List<HashMap<String, Object>> getRemitSubRecvList(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getRemitSubRecvList(hashmapParam);
    }
    

    //  transaction 처리 는 Procedure 에서 처리하도록 함
    public ReturnDataVO callProcRemitCancel(ProcRemitVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            // Van 스크래핑 데이터 이관 프로시저 호출
            withdrawMapper.callProcRemitCancel(procVo);
            
            // 프로시저에서 설정한 OUT 파라미터 확인
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

    public ReturnDataVO callProcRemitSend (ProcRemitVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            // Van 스크래핑 데이터 이관 프로시저 호출
            withdrawMapper.callProcRemitSend(procVo);
            
            // 프로시저에서 설정한 OUT 파라미터 확인
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


