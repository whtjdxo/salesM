package com.web.manage.loan.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
 

@Mapper
public interface LoanSubMapper {
    int getQueryTotalCnt();
    List<HashMap<String, Object>> getLoanSubSummary(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getLoanSubSummaryTotal(HashMap<String, Object> hashmapParam);
    
    List<HashMap<String, Object>> getChainLoanSubList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getChainLoanSubListTotal(HashMap<String, Object> hashmapParam);
}
