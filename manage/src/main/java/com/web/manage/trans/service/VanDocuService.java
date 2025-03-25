package com.web.manage.trans.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.trans.mapper.VanDocuMapper;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.trans.domain.TransProcessVO;
import com.web.manage.trans.domain.VanDocuVO;

@Service
public class VanDocuService {
    @Autowired
    private VanDocuMapper vanDocuMapper;

    public int getQueryTotalCnt() {
        return vanDocuMapper.getQueryTotalCnt();
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

    // public ReturnDataVO callScrapTransVanDocu(TransProcessVO procVo) {
    //     ReturnDataVO result = new ReturnDataVO();
    //     Map<String, Object> paramMap = new HashMap<>();
    //     paramMap.put("chainNo", procVo.getChainNo()); 
    //     paramMap.put("userId", procVo.getUserId());
    //     paramMap.put("resultCode", null);
    //     paramMap.put("resultMsg", null);

    //     try {
    //         // 프로시저 호출
    //         System.out.println("callScrapTransVanDocu");
    //         vanDocuMapper.callScrapTransVanDocu(paramMap);
    //         System.out.println("callScrapTransVanDocu end");    
            
    //         // 프로시저에서 설정한 OUT 파라미터 확인
    //         Integer resultCode = (Integer) paramMap.get("resultCode");
    //         String resultMsg = (String) paramMap.get("resultMsg");

    //         // 프로시저에서 설정한 OUT 파라미터 확인
    //         if (resultCode == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
    //             result.setResultCode("S000");
    //             result.setResultMsg(resultMsg);
    //         } else {
    //             result.setResultCode("F000");
    //             result.setResultMsg(resultMsg);
    //         }
    //     } catch (Exception e) {
    //         result.setResultCode("F500");
    //         result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
    //         System.out.println("Scrap transaction processing failed: " + e.getMessage());
    //         // 로깅 처리
    //         // logger.error("Scrap transaction processing failed", e);
    //     }
    //     return result;
    // }

    public ReturnDataVO callScrapTransVanDocu(TransProcessVO procVo) {
        // return vanDocuMapper.callScrapTransVanDocu(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            // 프로시저 호출
            vanDocuMapper.callScrapTransVanDocu(procVo);
            
            // 프로시저에서 설정한 OUT 파라미터 확인
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
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
