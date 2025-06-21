package com.web.manage.common.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	int getQueryTotalCnt(); 
    
    HashMap<String, Object> getSalesSummary(HashMap<String, Object> hashmapParam); 
    HashMap<String, Object> getDepositSummary(HashMap<String, Object> hashmapParam); 
    List<HashMap<String, Object>> getDailySummaryList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getBoardList(HashMap<String, Object> hashmapParam);

}
