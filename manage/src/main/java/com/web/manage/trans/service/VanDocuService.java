package com.web.manage.trans.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.trans.mapper.VanDocuMapper; 

import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor; 
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.trans.domain.MapCodeVO;
import com.web.manage.trans.domain.TransProcessVO; 

@Service
public class VanDocuService {
    @Autowired
    private VanDocuMapper vanDocuMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return vanDocuMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getMapPcodeList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getMapPcodeList(hashmapParam);
    }

    public List<HashMap<String, Object>> getMapCodeList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getMapCodeList(hashmapParam);
    }

    public boolean insertMapCode(MapCodeVO mapCodeVo ) {
        return vanDocuMapper.insertMapCode(mapCodeVo); 
    }
    public boolean updateMapCode(MapCodeVO mapCodeVo) {
        return vanDocuMapper.updateMapCode(mapCodeVo); 
    }

 
    public List<HashMap<String, Object>> getScrapDataSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getScrapDataSumm(hashmapParam);
    }

    public List<HashMap<String, Object>> getVanDocuSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getVanDocuSumm(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainDocuSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getChainDocuSumm(hashmapParam);
    } 

    public List<HashMap<String, Object>> getChainVanDocuList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getChainVanDocuList(hashmapParam);
    }

    public List<HashMap<String, Object>> getUnprocessedList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getUnprocessedList(hashmapParam);
    }

    public List<HashMap<String, Object>> getUnprocessedSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getUnprocessedSumm(hashmapParam);
    }
 
    //  transaction 처리 는 Procedure 에서 처리하도록 함
    public ReturnDataVO callScrapTransVanDocu(TransProcessVO procVo) {
        // return vanDocuMapper.callScrapTransVanDocu(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            // Van 스크래핑 데이터 이관 프로시저 호출
            vanDocuMapper.callScrapTransVanDocu(procVo);
            
            // 프로시저에서 설정한 OUT 파라미터 확인
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            }

            // Delivery 스크래핑 데이터 이관 프로시저 호출
            vanDocuMapper.callScrapTransDeliDocu(procVo);            
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
