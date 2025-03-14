package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.ChainMapper;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.mapper.UserMngMapper;
import com.web.manage.user.service.UserMngService;
import com.web.manage.base.domain.ChainVO;

@Service
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired 
    private UserMngService  userMngService;

    public List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return chainMapper.getQueryTotalCnt();
    }

    public String getNewChainNo() {
        return chainMapper.getNewChainNo();
    }

    public int getCeoIdDupChk(String ceo_id) {
        return chainMapper.getCeoIdDupChk(ceo_id);
    }

    public boolean insertChain(ChainVO chainVO, UserVO userVO) {
        if (!chainMapper.insertChain(chainVO)) {
            throw new RuntimeException("Agency Create failed");
        }
        if (!userMngService.userCreate(userVO)) {
            throw new RuntimeException("User(CEO) Create failed");
        }
        return true;        
    }

    public boolean updateChain(ChainVO chainVO, UserVO userVO) {
        if (!chainMapper.updateChain(chainVO)) {
            throw new RuntimeException("Agency Update failed"); 
        }
        if (!userMngService.userUpdate(userVO)) {
            throw new RuntimeException("User(CEO) Update failed"); 
        }
        return true;        
    }

    public boolean updateChainCont(ChainVO chainVO) {
        return chainMapper.updateChainCont(chainVO); 
    }

    public boolean updateChainVan(ChainVO chainVO) {
        return chainMapper.updateChainVan(chainVO); 
    }


    public boolean updateChainCard(ChainVO chainVO) {
        return chainMapper.updateChainCard(chainVO); 
    }

    public boolean updateChainFile(ChainVO chainVO) {
        return chainMapper.updateChainFile(chainVO); 
    }
}
