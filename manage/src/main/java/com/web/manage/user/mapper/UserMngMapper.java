package com.web.manage.user.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMngMapper {

    List<HashMap<String, Object>> userMngListRetrieve(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam);
    
    int getUserIdDupChk(String user_id);
    
    boolean userCreate(HashMap<String, Object> hashmapParam);

    boolean userUpdate(HashMap<String, Object> hashmapParam);
}

