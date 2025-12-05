package com.web.manage.common.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.common.mapper.CommonMapper;

@Service
public class CommonService {

    @Autowired
    private CommonMapper commonMapper;

    public int getQueryTotalCnt() {
        return commonMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getTotalCodelist(HashMap<String, String> hashmapParam) {
        return commonMapper.getTotalCodelist(hashmapParam);
    }

    public List<HashMap<String, Object>> getCreditCorpList(HashMap<String, String> hashmapParam) {
        return commonMapper.getCreditCorpList(hashmapParam);
    }

    public List<HashMap<String, Object>> getOpCorpList(HashMap<String, String> hashmapParam) {
        return commonMapper.getOpCorpList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainVanList(HashMap<String, String> hashmapParam) {
        return commonMapper.getChainVanList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainCardList(HashMap<String, String> hashmapParam) {
        return commonMapper.getChainCardList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainList(HashMap<String, String> hashmapParam) {
        return commonMapper.getChainList(hashmapParam);
    }    

    public List<HashMap<String, Object>> getLinkChainList(HashMap<String, Object> hashmapParam){
        return commonMapper.getLinkChainList(hashmapParam);
    }  

    public List<HashMap<String, Object>> getShiftChainList(HashMap<String, String> hashmapParam) {
        return commonMapper.getShiftChainList(hashmapParam);
    }    

    public List<HashMap<String, Object>> getAgencyList(HashMap<String, String> hashmapParam) {
        return commonMapper.getAgencyList(hashmapParam);
    }

    public String getPreWorkDay() {
        return commonMapper.getPreWorkDay();
    }
    
    public String getNextWorkDay() {
        return commonMapper.getNextWorkDay();
    }

    public String getNearWorkDay() {
        return commonMapper.getNearWorkDay();
    }

    public String getToDay() {
        return commonMapper.getToDay();
    }

}
