package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.AgencyVO;

@Mapper
public interface AgencyMapper {
    
    List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    String createAgencyNo();
    
    int getCeoIdDupChk(String ceo_id);

    boolean agencyCreate(AgencyVO agencyVO);

    boolean agencyUpdate(AgencyVO agencyVO);
}

