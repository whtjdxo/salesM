package com.web.manage.center.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CenterMngMapper {

    List<HashMap<String, Object>> centerMngListRetrieve(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    HashMap<String, Object> getBossInfo(String boss_id);

    boolean bossCreate(HashMap<String, Object> hashmapParam);

    boolean centerCreate(HashMap<String, Object> hashmapParam);

    boolean bossUpdate(HashMap<String, Object> hashmapParam);

    boolean centerUpdate(HashMap<String, Object> hashmapParam);
}


