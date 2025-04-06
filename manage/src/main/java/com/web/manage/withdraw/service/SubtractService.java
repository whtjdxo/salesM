package com.web.manage.withdraw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.web.manage.withdraw.domain.SubMstVO;
import com.web.manage.withdraw.mapper.SubtractMapper;
import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor;

@Service
public class SubtractService {
    @Autowired
    private SubtractMapper subtractMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return subtractMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getSubSummary(HashMap<String, Object> hashmapParam) {
        return subtractMapper.getSubSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainSubList(HashMap<String, Object> hashmapParam) {
        return subtractMapper.getChainSubList(hashmapParam);
    }

    public String getNewSubNo() {
        return subtractMapper.getNewSubNo();
    }
    
    public boolean insertSubMst(SubMstVO subMstVo ) { 
        return subtractMapper.insertSubMst(subMstVo); 
    }
    public boolean updateSubMst(SubMstVO subMstVo) {
        return subtractMapper.updateSubMst(subMstVo); 
    }

  
    //  transaction 처리 는 Procedure 에서 처리하도록 함
    // public ReturnDataVO callScrapTransVanDocu(TransProcessVO procVo) {
    //     // return subtractMapper.callScrapTransVanDocu(procVo);
    //     ReturnDataVO result = new ReturnDataVO();
    //     try {
    //         // Van 스크래핑 데이터 이관 프로시저 호출
    //         subtractMapper.callScrapTransVanDocu(procVo);
            
    //         // 프로시저에서 설정한 OUT 파라미터 확인
    //         if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
    //             result.setResultCode("S000");
    //             result.setResultMsg(procVo.getResultMsg());
    //         } else {
    //             result.setResultCode("F000");
    //             result.setResultMsg(procVo.getResultMsg());
    //             return result;
    //         }

    //         // Delivery 스크래핑 데이터 이관 프로시저 호출
    //         subtractMapper.callScrapTransDeliDocu(procVo);            
    //         // 프로시저에서 설정한 OUT 파라미터 확인
    //         if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
    //             result.setResultCode("S000");
    //             result.setResultMsg(procVo.getResultMsg());
    //         } else {
    //             result.setResultCode("F000");
    //             result.setResultMsg(procVo.getResultMsg());
    //             return result;
    //         }
    //     } catch (Exception e) {
    //         result.setResultCode("F500");
    //         result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
    //         // 로깅 처리
    //         // logger.error("Scrap transaction processing failed", e);
    //     }
    //     return result;
    // }

}
