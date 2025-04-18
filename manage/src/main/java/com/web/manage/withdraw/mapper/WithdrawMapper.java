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
    List<HashMap<String, Object>> getWDChainSummary(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getWDCardSummary(HashMap<String, Object> hashmapParam);    
    List<HashMap<String, Object>> getWDResvList(HashMap<String, Object> hashmapParam);
    
    void callPrcRemitMain(ProcRemitVO procVo);

    boolean changeWorkDate(ProcRemitVO procVo);
    boolean changeWdStatus(ProcRemitVO procVo);
    
}

