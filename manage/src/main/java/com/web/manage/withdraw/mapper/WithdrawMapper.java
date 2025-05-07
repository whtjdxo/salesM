package com.web.manage.withdraw.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper; 
import com.web.manage.withdraw.domain.ProcRemitVO; 
 
@Mapper
public interface WithdrawMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getWDSummary(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getWDSummaryTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getWDChainSummary(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getWDCardSummary(HashMap<String, Object> hashmapParam);    
    List<HashMap<String, Object>> getWDResvList(HashMap<String, Object> hashmapParam);
    
    void callProcRemitMain(ProcRemitVO procVo);

    boolean changeWorkDate(ProcRemitVO procVo);
    boolean changeWdStatus(ProcRemitVO procVo);

    List<HashMap<String, Object>> getRemitSummary(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getRemitSummaryTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getRemitList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getRemitSubRecvList(HashMap<String, Object> hashmapParam);
    
    void callProcRemitCancel(ProcRemitVO procVo);
    void callProcRemitSend(ProcRemitVO procVo);
}

