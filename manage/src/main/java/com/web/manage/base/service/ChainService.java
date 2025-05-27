package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.ChainMapper;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.base.domain.ChainCounselVO;
import com.web.manage.base.domain.ChainFileVO;
import com.web.manage.base.domain.ChainVO;
import com.web.manage.base.domain.ChainVanVO;
import com.web.manage.base.domain.LinkChainVO;

@Service
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired 
    private UserMngService  userMngService;

    public List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainVanList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainVanList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainCardList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainCardList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainFileList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainFileList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return chainMapper.getQueryTotalCnt();
    }

    public String getNewChainNo() {
        return chainMapper.getNewChainNo();
    }
 
    public int getVanIdDupChk(HashMap<String, Object> params) {
        return chainMapper.getVanIdDupChk(params);
    }

    public int getCardDupChk(HashMap<String, Object> params) {
        return chainMapper.getCardDupChk(params);
    } 

    public int getFileDupChk(HashMap<String, Object> params) {
        return chainMapper.getFileDupChk(params);
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

    public boolean insertChainVan(ChainVanVO chainVanVO ) {
        return chainMapper.insertChainVan(chainVanVO); 
    }
    public boolean updateChainVan(ChainVanVO chainVanVO) {
        return chainMapper.updateChainVan(chainVanVO); 
    }

    public boolean insertChainCard(ChainCardVO chainCardVo ) {
        return chainMapper.insertChainCard(chainCardVo); 
    }
    public boolean updateChainCard(ChainCardVO chainCardVo) {
        return chainMapper.updateChainCard(chainCardVo); 
    }

    public List<HashMap<String, Object>> getLinkChainList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getLinkChainList(hashmapParam);
    }
    public boolean insertLinkChain(LinkChainVO linkChainVO ) {
        return chainMapper.insertLinkChain(linkChainVO); 
    }
    public boolean updateLinkChain(LinkChainVO linkChainVO) {
        return chainMapper.updateLinkChain(linkChainVO); 
    }
  
    public String getNewFileNo() {
        return chainMapper.getNewFileNo();
    }

    public boolean insertChainFile(ChainFileVO chainFileVo ) {
        return chainMapper.insertChainFile(chainFileVo); 
    }
    public boolean updateChainFile(ChainFileVO chainFileVo) {
        return chainMapper.updateChainFile(chainFileVo); 
    }
    public boolean deleteChainFile(ChainFileVO chainFileVo) {
        return chainMapper.deleteChainFile(chainFileVo); 
    }

    public List<HashMap<String, Object>> getChainCounselList(HashMap<String, Object> hashmapParam) {
        return chainMapper.getChainCounselList(hashmapParam);
    }

    public boolean insertChainCounsel(ChainCounselVO counselVo) {
        return chainMapper.insertChainCounsel(counselVo); 
    }
    public boolean updateChainCounsel(ChainCounselVO counselVo) {
        return chainMapper.updateChainCounsel(counselVo); 
    }
    public boolean deleteChainCounsel(ChainCounselVO counselVo) {
        return chainMapper.deleteChainCounsel(counselVo); 
    }

 
}
