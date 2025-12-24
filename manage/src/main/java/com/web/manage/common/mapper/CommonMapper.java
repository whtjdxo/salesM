package com.web.manage.common.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.common.domain.JobSeqVO;

@Mapper
public interface CommonMapper {
    int getQueryTotalCnt();
    List<HashMap<String, Object>> getTotalCodelist(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getUserAuthGroupList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getCreditCorpList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getOpCorpList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getChainVanList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getChainCardList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getChainList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getChainSchList(String keyword);
    
    List<HashMap<String, Object>> getLinkChainList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getShiftChainList(HashMap<String, String> hashmapParam);

    List<HashMap<String, Object>> getAgencyList(HashMap<String, String> hashmapParam);

    String getPreWorkDay();
    String getNextWorkDay();
    String getNearWorkDay();
    String getToDay();

    void callProcGetJobSeq(JobSeqVO jobSeqVo) ;

}
    