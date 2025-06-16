package com.web.manage.statis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonthlyReportMapper {
    int getQueryTotalCnt(); 
    
    List<HashMap<String, Object>> getMonthlySummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getMonthlySummaryTotal(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getMonthlyList(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getMonthlyListTotal(HashMap<String, Object> hashmapParam);    

}
