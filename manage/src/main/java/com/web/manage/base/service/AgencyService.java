package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.AgencyMapper;
import com.web.manage.base.domain.AgencyVO;

@Service
public class AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;

    public List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> hashmapParam) {
        return agencyMapper.getAgencyList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return agencyMapper.getQueryTotalCnt();
    }

    public String createAgencyNo() {
        return agencyMapper.createAgencyNo();
    }

    public int getCeoIdDupChk(String ceo_id) {
        return agencyMapper.getCeoIdDupChk(ceo_id);
    }

    public boolean agencyCreate(AgencyVO agencyVO) {
        return agencyMapper.agencyCreate(agencyVO);
    }

    public boolean agencyUpdate(AgencyVO agencyVO) {
        return agencyMapper.agencyUpdate(agencyVO);
    }
}
