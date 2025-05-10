package com.web.manage.deposit.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.ExceedMstVO;
// Ensure the correct package path for ExceedMstVO
import com.web.manage.deposit.domain.ExceedMstVO; // Update this path if the class is located elsewhere


@Mapper
public interface ExceedMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getExcSummary(HashMap<String, Object> hashmapParam);    
    HashMap<String, Object> getExcSummTotal(HashMap<String, Object> hashmapParam);
    
    List<HashMap<String, Object>> getChainExcList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getChainExcListTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getExcReceiveList(HashMap<String, Object> hashmapParam);    

    String getNewExcNo();    
    boolean insertExceedMst(ExceedMstVO exceedMstVo);
    boolean updateExceedMst(ExceedMstVO exceedMstVo);   
}

