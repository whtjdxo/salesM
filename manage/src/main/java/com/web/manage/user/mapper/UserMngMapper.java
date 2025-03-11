package com.web.manage.user.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.web.manage.user.domain.UserVO;

import jakarta.validation.Valid;

@Mapper
public interface UserMngMapper {

    List<HashMap<String, Object>> userMngListRetrieve(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam);
    
    int getUserIdDupChk(String user_id);
    
    boolean userCreate(@ModelAttribute("UserVO") @Valid UserVO userVO);

    boolean userUpdate(@ModelAttribute("UserVO") @Valid UserVO userVO);
}

