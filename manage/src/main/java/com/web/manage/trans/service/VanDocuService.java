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

    public int getQueryTotalCnt() {
        return vanDocuMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getScrapDataSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getScrapDataSumm(hashmapParam);
    }

    public List<HashMap<String, Object>> getVanDocuSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getVanDocuSumm(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainDocuSumm(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getChainDocuSumm(hashmapParam);
    } 

    public List<HashMap<String, Object>> getChainVanDocuList(HashMap<String, Object> hashmapParam) {
        return vanDocuMapper.getChainVanDocuList(hashmapParam);
    }

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
