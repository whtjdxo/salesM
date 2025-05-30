package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.base.domain.ChainCounselVO;
import com.web.manage.base.domain.ChainFileVO;
import com.web.manage.base.domain.ChainVO;
import com.web.manage.base.domain.ChainVanVO;
import com.web.manage.base.domain.LinkChainVO;

@Mapper
public interface ChainMapper {

    List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainVanList(HashMap<String, Object> hashmapParam);
    
    List<HashMap<String, Object>> getChainFileList(HashMap<String, Object> hashmapParam);
 

    int getQueryTotalCnt();

    String getNewChainNo();
     
    int getVanIdDupChk(HashMap<String, Object> params);

    int getCardDupChk(HashMap<String, Object> params);
    int getFileDupChk(HashMap<String, Object> params);

    boolean insertChain(ChainVO chainVO);

    boolean updateChain(ChainVO chainVO);
    boolean updateChainCont(ChainVO chainVO);
    
    boolean insertChainVan(ChainVanVO chainVanVO);
    boolean updateChainVan(ChainVanVO chainVanVO);

    List<HashMap<String, Object>> getChainCardList(HashMap<String, Object> hashmapParam);
    boolean insertChainCard(ChainCardVO chainCardVo);
    boolean updateChainCard(ChainCardVO chainCardVo);

    List<HashMap<String, Object>> getLinkChainList(HashMap<String, Object> hashmapParam);    
    boolean insertLinkChain(LinkChainVO linkChainVo);
    boolean updateLinkChain(LinkChainVO linkChainVo);

    String getNewFileNo();
    boolean insertChainFile(ChainFileVO chainFileVo);
    boolean updateChainFile(ChainFileVO chainFileVo);
    boolean deleteChainFile(ChainFileVO chainFileVo);
 
    List<HashMap<String, Object>> getChainCounselList(HashMap<String, Object> hashmapParam);     
    boolean insertChainCounsel(ChainCounselVO counselVo);
    boolean updateChainCounsel(ChainCounselVO counselVo);
    boolean deleteChainCounsel(ChainCounselVO counselVo);
}
