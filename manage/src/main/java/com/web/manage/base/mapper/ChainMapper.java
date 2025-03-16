package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.base.domain.ChainVO;
import com.web.manage.base.domain.ChainVanVO;

@Mapper
public interface ChainMapper {

    List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainVanList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainCardList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainFileList(HashMap<String, Object> hashmapParam);
 

    int getQueryTotalCnt();

    String getNewChainNo();
     
    int getVanIdDupChk(HashMap<String, Object> params);

    int getCardDupChk(HashMap<String, Object> params);

    boolean insertChain(ChainVO chainVO);

    boolean updateChain(ChainVO chainVO);
    boolean updateChainCont(ChainVO chainVO);
    
    boolean insertChainVan(ChainVanVO chainVanVO);
    boolean updateChainVan(ChainVanVO chainVanVO);

    boolean insertChainCard(ChainCardVO chainCardVo);
    boolean updateChainCard(ChainCardVO chainCardVo);

    boolean insertChainFile(ChainVO chainVO);
    boolean updateChainFile(ChainVO chainVO);
}
