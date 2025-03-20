package com.web.manage.trans.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.trans.domain.VanDocuVO;

@Mapper
public interface VanDocuMapper {
    
    List<HashMap<String, Object>> getChainSummList(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();


    boolean insertAgency(VanDocuVO vanDocuVO);

    boolean updateVanDocu(VanDocuVO vanDocuVO);
    
}

