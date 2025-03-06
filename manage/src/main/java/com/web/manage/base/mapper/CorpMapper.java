package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.base.domain.CorpVO;

@Mapper
public interface CorpMapper {

    List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    String createCorpCd();

    boolean corpCreate(CorpVO corpVO);

    boolean corpUpdate(CorpVO corpVO);
}
