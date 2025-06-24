package com.web.manage.common.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
    int getQueryTotalCnt();
    List<HashMap<String, Object>> getTotalCodelist(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getCreditCorpList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getChainList(HashMap<String, String> hashmapParam);
    
    List<HashMap<String, Object>> getLinkChainList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getShiftChainList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getAgencyList(HashMap<String, String> hashmapParam);

    String getPreWorkDay();
    String getNextWorkDay();
    String getNearWorkDay();
    String getToDay();

}
