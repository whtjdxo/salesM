package com.web.manage.withdraw.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface WithdrawMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getWithdrawSummary(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChianWithdrawList(HashMap<String, Object> hashmapParam);    
    List<HashMap<String, Object>> getWithdrawDetailList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getWithdrawList(HashMap<String, Object> hashmapParam);
    
}

