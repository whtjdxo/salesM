package com.web.manage.center.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.web.manage.center.mapper.CenterMngMapper;

@Service
public class CenterMngService {
    @Autowired
    private CenterMngMapper centerMngMapper;

    public List<HashMap<String, Object>> centerMngListRetrieve(HashMap<String, Object> hashmapParam) {
      return centerMngMapper.centerMngListRetrieve(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return centerMngMapper.getQueryTotalCnt();  
    }

    public HashMap<String, Object> getBossInfo(String boss_id) {
        return centerMngMapper.getBossInfo(boss_id);
    }

    public boolean centerCreate(HashMap<String, Object> hashmapParam) {
        return centerMngMapper.centerCreate(hashmapParam);
    }

    public boolean bossCreate(HashMap<String, Object> hashmapParam) {
        return centerMngMapper.bossCreate(hashmapParam);
    }

    public boolean centerUpdate(HashMap<String, Object> hashmapParam) {
        return centerMngMapper.centerUpdate(hashmapParam);
    }
    public boolean bossUpdate(HashMap<String, Object> hashmapParam) {
        return centerMngMapper.bossUpdate(hashmapParam);
    }
}
