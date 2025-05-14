package com.web.manage.deposit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.web.manage.deposit.domain.ExceedMstVO;
import com.web.manage.deposit.mapper.ExceedMapper;
import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.common.domain.ReturnDataVO;
import ch.qos.logback.classic.Logger;

import com.web.config.interceptor.AuthInterceptor;

@Service
public class ExceedService {
    @Autowired
    private ExceedMapper exceedMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return exceedMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getExcSummary(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getExcSummary(hashmapParam);
    }

    public HashMap<String, Object> getExcSummTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getExcSummTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainExcList(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcList(hashmapParam);
    }

    public HashMap<String, Object> getChainExcListTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcListTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainExcResvList(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcResvList(hashmapParam);
    }

    public HashMap<String, Object> getChainExcResvListTotal(HashMap<String, Object> hashmapParam) {
        return exceedMapper.getChainExcResvListTotal(hashmapParam);
    }
  
    public String getNewExcNo() {
        return exceedMapper.getNewExcNo();
    }    
    
    public boolean insertExceedMst(ExceedMstVO exceedMstVo ) {
        return exceedMapper.insertExceedMst(exceedMstVo); 
    }
    public boolean updateExceedMst(ExceedMstVO exceedMstVo ) {
        return exceedMapper.updateExceedMst(exceedMstVo); 
    }

}
