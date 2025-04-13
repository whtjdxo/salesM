package com.web.manage.withdraw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.withdraw.mapper.WithdrawMapper; 
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;

import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.base.domain.ChainVanVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.trans.domain.MapCodeVO;
import com.web.manage.trans.domain.TransProcessVO;
import com.web.manage.trans.domain.VanDocuVO;

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

    public List<HashMap<String, Object>> getWDChainSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDChainSummary(hashmapParam);
    } 
    
    public List<HashMap<String, Object>> getWDCardSummary(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDCardSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getWdResvlList(HashMap<String, Object> hashmapParam) {
        return withdrawMapper.getWDResvList(hashmapParam);
    }
 
 
    // //  transaction 처리 는 Procedure 에서 처리하도록 함
    // public ReturnDataVO callScrapTransVanDocu(TransProcessVO procVo) {
    //     // return withdrawMapper.callScrapTransVanDocu(procVo);
    //     ReturnDataVO result = new ReturnDataVO();
    //     try {
    //         // Van 스크래핑 데이터 이관 프로시저 호출
    //         withdrawMapper.callScrapTransVanDocu(procVo);
            
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
    //         withdrawMapper.callScrapTransDeliDocu(procVo);            
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

}
