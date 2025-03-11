package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.AgencyVO;

@Mapper
public interface AgencyMapper {
    
    List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    String getNewAgencyNo();
    
    int getCeoIdDupChk(String ceo_id);

    boolean InsertAgency(AgencyVO agencyVO);

    boolean UpdateAgency(AgencyVO agencyVO);
    boolean UpdateAgencyCont(AgencyVO agencyVO);
}

