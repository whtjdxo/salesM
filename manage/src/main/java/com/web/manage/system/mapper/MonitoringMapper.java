package com.web.manage.system.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitoringMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getBatchList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getBatchDetailList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getScrapList(HashMap<String, Object> hashmapParam);
    int updateScrapClear(HashMap<String, Object> hashmapParam);
}
