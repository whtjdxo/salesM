package com.web.manage.deposit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.web.manage.deposit.domain.ExceedMstVO;
import com.web.manage.deposit.domain.ProcExceedVO;
import com.web.manage.deposit.mapper.ExceedMapper;
import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.common.domain.ReturnDataVO;
import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor;

@Service
public class ExceedService {
    @Autowired
    private ExceedMapper exceedMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return exceedMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getExcSummary(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getExcSummary(hashmapParam);
    }

    public HashMap<String, Object> getExcSummTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getExcSummTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainExcList(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcList(hashmapParam);
    }

    public HashMap<String, Object> getChainExcListTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcListTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainExcResvList(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcResvList(hashmapParam);
    }

    public HashMap<String, Object> getChainExcResvListTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcResvListTotal(hashmapParam);
    }
  
    public String getNewExcNo() {
        return exceedMapper.getNewExcNo();
    }    
    
    public boolean insertExceedMst(ExceedMstVO exceedMstVo ) {
        return exceedMapper.insertExceedMst(exceedMstVo); 
    }
    public ReturnDataVO updateExceedMst(ExceedMstVO exceedMstVo ) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            int updated = exceedMapper.updateExceedMst(exceedMstVo);         
            if (updated >= 0) {             // 오류가 아닌 이상 update 가 0 이상이면 성공
                result.setResultCode("S000");
                result.setResultMsg("정상 처리되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("데이터 변경에 실패했습니다.");
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result;  
    }

    public ReturnDataVO setExceedReady(ProcExceedVO procVo) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            int updated = exceedMapper.setExceedReady(procVo);         
            if (updated >= 0) {             // 오류가 아닌 이상 update 가 0 이상이면 성공
                result.setResultCode("S000");
                result.setResultMsg(updated + "건이 Ready 상태로 변경되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("데이터 변경에 실패했습니다.");
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result; 
    } 

    public ReturnDataVO setExceedReadyAll(ProcExceedVO procVo ) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            int updated = exceedMapper.setExceedReadyAll(procVo);         
            if (updated >= 0) {             // 오류가 아닌 이상 update 가 0 이상이면 성공
                result.setResultCode("S000");
                result.setResultMsg(updated + "건이 Ready 상태로 변경되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("데이터 변경에 실패했습니다.");
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result; 
    }

    public ReturnDataVO setExceedCancel(ProcExceedVO procVo) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            int updated = exceedMapper.setExceedCancel(procVo);         
            if (updated >= 0) {             // 오류가 아닌 이상 update 가 0 이상이면 성공
                result.setResultCode("S000");
                result.setResultMsg(updated + "건이 미출금 상태로 변경되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("데이터 변경에 실패했습니다.");
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result; 
    }

    public ReturnDataVO setExceedCancelAll(ProcExceedVO procVo ) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            int updated = exceedMapper.setExceedCancelAll(procVo);         
            if (updated >= 0) {             // 오류가 아닌 이상 update 가 0 이상이면 성공
                result.setResultCode("S000");
                result.setResultMsg(updated + "건이 미출금 상태로 변경되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("데이터 변경에 실패했습니다.");
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result; 
    }

}
