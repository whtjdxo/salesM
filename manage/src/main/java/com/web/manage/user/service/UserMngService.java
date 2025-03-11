package com.web.manage.user.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.web.manage.user.domain.UserVO;
import com.web.manage.user.mapper.UserMngMapper;

import jakarta.validation.Valid;

@Service
public class UserMngService {

    @Autowired
    private UserMngMapper userMngMapper;

    public List<HashMap<String, Object>> userMngListRetrieve(HashMap<String, Object> hashmapParam) {
        return userMngMapper.userMngListRetrieve(hashmapParam);
    }


    public int getQueryTotalCnt() {
        return userMngMapper.getQueryTotalCnt();
    }

    public int getUserIdDupChk(String user_id) {
        return userMngMapper.getUserIdDupChk(user_id);
    }
    
    public List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam) {
        return userMngMapper.getCorpList(hashmapParam);
    }


    public boolean userCreate(@ModelAttribute("UserVO") @Valid UserVO userVO) {
        return userMngMapper.userCreate(userVO);
    }


    public boolean userUpdate(@ModelAttribute("UserVO") @Valid UserVO userVO) {
        return userMngMapper.userUpdate(userVO);
    }
}
