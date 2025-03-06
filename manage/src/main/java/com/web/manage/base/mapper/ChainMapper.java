package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.ChainVO;

@Mapper
public interface ChainMapper {

    List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    String createChainNo();
    
    int getCeoIdDupChk(String ceo_id);

    boolean chainCreate(ChainVO chainVO);

    boolean chainUpdate(ChainVO chainVO);
}
