package com.web.manage.trans.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.trans.domain.TransProcessVO;
import com.web.manage.trans.domain.VanDocuVO;

@Mapper
public interface VanDocuMapper {
    
    List<HashMap<String, Object>> getScrapDataSumm(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getVanDocuSumm(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainDocuSumm(HashMap<String, Object> hashmapParam);
    
    List<HashMap<String, Object>> getChainVanDocuList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getUnprocessedList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getUnprocessedSumm(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt(); 
    
    // void callScrapTransVanDocu(TransProcessVO procVo);
    void callScrapTransVanDocu(TransProcessVO procVo);    
    void callScrapTransVanDocu(Map<String, Object> paramMap );    
    
}

