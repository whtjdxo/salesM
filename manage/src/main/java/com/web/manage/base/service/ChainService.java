package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.ChainMapper;
import com.web.manage.base.domain.ChainVO;

@Service
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;

    public List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return chainMapper.getQueryTotalCnt();
    }

    public String createChainNo() {
        return chainMapper.createChainNo();
    }

    public int getCeoIdDupChk(String ceo_id) {
        return chainMapper.getCeoIdDupChk(ceo_id);
    }

    public boolean chainCreate(ChainVO chainVO) {
        return chainMapper.chainCreate(chainVO);
    }

    public boolean chainUpdate(ChainVO chainVO) {
        return chainMapper.chainUpdate(chainVO);
    }
}
