package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.ChainVO;

@Mapper
public interface ChainMapper {

    List<HashMap<String, Object>> getChainList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainVanList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainCardList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getChainFileList(HashMap<String, Object> hashmapParam);
 

    int getQueryTotalCnt();

    String getNewChainNo();
    
    int getCeoIdDupChk(String ceo_id);

    boolean insertChain(ChainVO chainVO);

    boolean updateChain(ChainVO chainVO);
    boolean updateChainCont(ChainVO chainVO);
    boolean updateChainVan(ChainVO chainVO);
    boolean updateChainCard(ChainVO chainVO);
    boolean updateChainFile(ChainVO chainVO);
}
