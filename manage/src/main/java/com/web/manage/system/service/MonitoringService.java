package com.web.manage.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.mapper.MonitoringMapper;

@Service
public class MonitoringService {
    @Autowired
    private MonitoringMapper monitoringMapper;
    
    public int getQueryTotalCnt() {
        return monitoringMapper.getQueryTotalCnt();
    }   
    public List<HashMap<String, Object>> getBatchList(HashMap<String, Object> hashmapParam) {
        return monitoringMapper.getBatchList(hashmapParam);
    }
    public List<HashMap<String, Object>> getBatchDetailList(HashMap<String, Object> hashmapParam) {
        return monitoringMapper.getBatchDetailList(hashmapParam);
    }

    public List<HashMap<String, Object>> getScrapList(HashMap<String, Object> hashmapParam) {
        return monitoringMapper.getScrapList(hashmapParam);
    }

}
