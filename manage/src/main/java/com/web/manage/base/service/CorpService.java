package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.CorpMapper;
import com.web.manage.base.domain.CorpVO;

import jakarta.validation.Valid;

@Service
public class CorpService {

    @Autowired
    private CorpMapper corpMapper;

    public List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam) {
        return corpMapper.getCorpList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return corpMapper.getQueryTotalCnt();
    }

    public String createCorpCd() {
        return corpMapper.createCorpCd();
    }

    public boolean corpCreate(@Valid CorpVO corpVO) {        
		return corpMapper.corpCreate(corpVO);
	}
 

    public boolean corpUpdate(@Valid CorpVO corpVO) {
        return corpMapper.corpUpdate(corpVO);
    }
}
