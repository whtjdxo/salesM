package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.base.mapper.AgencyMapper;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
import com.web.manage.base.domain.AgencyVO;

@Service
public class AgencyService {
    @Autowired
    private UserMngService userMngService;

    @Autowired
    private AgencyMapper agencyMapper;

    public List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> hashmapParam) {
        return agencyMapper.getAgencyList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return agencyMapper.getQueryTotalCnt();
    }

    public String getNewAgencyNo() {
        return agencyMapper.getNewAgencyNo();
    }

    public int getCeoIdDupChk(String ceo_id) {
        return agencyMapper.getCeoIdDupChk(ceo_id);
    }

    @Transactional
    public boolean InsertAgency(AgencyVO agencyVO, UserVO userVO) {
        if (!agencyMapper.InsertAgency(agencyVO)) {
            throw new RuntimeException("Agency Create failed");
        }
        if (!userMngService.userCreate(userVO)) {
            throw new RuntimeException("User(CEO) Create failed");
        }
        return true;
        // return agencyMapper.InsertAgency(agencyVO);
    }

    @Transactional
    public boolean UpdateAgency(AgencyVO agencyVO, UserVO userVO) {
        
        if (!agencyMapper.UpdateAgency(agencyVO)) {
            throw new RuntimeException("Agency Update failed"); 
        }
        if (!userMngService.userUpdate(userVO)) {
            throw new RuntimeException("User(CEO) Update failed"); 
        }
        return true;
        // return agencyMapper.UpdateAgency(agencyVO);
    }

    public boolean UpdateAgencyCont(AgencyVO agencyVO) {
        return agencyMapper.UpdateAgencyCont(agencyVO);
    }
}
