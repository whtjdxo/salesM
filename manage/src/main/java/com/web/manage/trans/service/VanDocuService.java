package com.web.manage.trans.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.trans.mapper.VanDocuMapper;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
import com.web.manage.trans.domain.VanDocuVO;

@Service
public class VanDocuService {
    @Autowired
    private VanDocuMapper vanDocuMapper;

    public List<HashMap<String, Object>> getgetChainSummListVanList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getChainSummList(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return vanDocuMapper.getQueryTotalCnt();
    }

    // public String getNewVanDocuNo() {
    //     return vanDocuMapper.getNewVanDocuNo();
    // }

    // public int getCeoIdDupChk(String ceo_id) {
    //     return vanDocuMapper.getCeoIdDupChk(ceo_id);
    // }

    // @Transactional
    // public boolean InsertVanDocu(VanDocuVO vanDocuVO, UserVO userVO) {
    //     if (!vanDocuMapper.InsertVanDocu(vanDocuVO)) {
    //         throw new RuntimeException("VanDocu Create failed");
    //     }
    //     if (!userMngService.userCreate(userVO)) {
    //         throw new RuntimeException("User(CEO) Create failed");
    //     }
    //     return true;
    //     // return vanDocuMapper.InsertVanDocu(vanDocuVO);
    // }

    // @Transactional
    // public boolean UpdateVanDocu(VanDocuVO vanDocuVO, UserVO userVO) {
        
    //     if (!vanDocuMapper.UpdateVanDocu(vanDocuVO)) {
    //         throw new RuntimeException("VanDocu Update failed"); 
    //     }
    //     if (!userMngService.userUpdate(userVO)) {
    //         throw new RuntimeException("User(CEO) Update failed"); 
    //     }
    //     return true;
    //     // return vanDocuMapper.UpdateVanDocu(vanDocuVO);
    // }

    // public boolean UpdateVanDocuCont(VanDocuVO vanDocuVO) {
    //     return vanDocuMapper.UpdateVanDocuCont(vanDocuVO);
    // }
}
