package com.web.manage.statis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyReportMapper {
    int getQueryTotalCnt(); 
    
    List<HashMap<String, Object>> getDailySummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDailySummaryTotal(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDailyList(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDailyListTotal(HashMap<String, Object> hashmapParam);    

}
