package com.web.manage.billing.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgencyFeeMapper {
    int getQueryTotalCnt(); 
    
    List<HashMap<String, Object>> getAgencyFeeSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getAgencyFeeSummaryTotal(HashMap<String, Object> hashmapParam); 

    List<HashMap<String, Object>> getAgencyFeeList(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getAgencyFeeListTotal(HashMap<String, Object> hashmapParam); 

    List<HashMap<String, Object>> getChainTaxExcel(HashMap<String, Object> hashmapParam);    
}
