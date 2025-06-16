package com.web.manage.deposit.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.deposit.domain.ProcDayCloseVO;
import com.web.manage.deposit.domain.ProcDepositVO;
import com.web.manage.deposit.mapper.DayCloseMapper;

import ch.qos.logback.classic.Logger;

@Service
public class DayCloseService {
    @Autowired
    private DayCloseMapper dayCloseMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    
    public int getQueryTotalCnt() {
        return dayCloseMapper.getQueryTotalCnt();
    }   
    public HashMap<String, Object> getDayCloseCheck(HashMap<String, Object> hashmapParam) {
        return dayCloseMapper.getDayCloseCheck(hashmapParam);
    }

    public List<HashMap<String, Object>> getDayCloseList(HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> closeChk  = new HashMap<String, Object>();
        closeChk =  dayCloseMapper.getDayCloseCheck(hashmapParam);
        // System.out.println(" close_chk >>>>>>>>>> " + closeChk.get("close_chk"));            
        if ("CLOSE".equals(closeChk.get("close_chk"))) {
            return dayCloseMapper.getDayCloseList(hashmapParam);
        } else {
            return dayCloseMapper.getDayPreCloseList(hashmapParam);
        }        
    }
     
    public HashMap<String, Object> getDayCloseSumm(HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> closeChk  = new HashMap<String, Object>();
        closeChk =  dayCloseMapper.getDayCloseCheck(hashmapParam);

        if ("CLOSE".equals(closeChk.get("close_chk"))) {
            return dayCloseMapper.getDayCloseSumm(hashmapParam);
        } else {
            return dayCloseMapper.getDayPreCloseSumm(hashmapParam);
        }
    }

    public ReturnDataVO callProcDayClose(ProcDayCloseVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            dayCloseMapper.callProcDayClose(procVo);            
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

    public ReturnDataVO callProcDayCloseCancel(ProcDayCloseVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            dayCloseMapper.callProcDayCloseCancel(procVo);            
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
        }
        return result; 
    }

}
