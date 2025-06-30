package com.web.manage.statis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesReportMapper {
    int getQueryTotalCnt(); 
    
    List<HashMap<String, Object>> getSalesSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getSalesSummaryTotal(HashMap<String, Object> hashmapParam); 

    List<HashMap<String, Object>> getSalesTransition(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getChainSalesTransition(HashMap<String, Object> hashmapParam); 
    HashMap<String, Object> getSalesTransitionTotal(HashMap<String, Object> hashmapParam); 
}
