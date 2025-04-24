package com.web.manage.deposit.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.deposit.mapper.DepositMapper;

import ch.qos.logback.classic.Logger;

@Service
public class DepositService {

    @Autowired
    private DepositMapper depositMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    public int getQueryTotalCnt() {
        return depositMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoCardSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepositList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositList(hashmapParam);
    }

    

    public boolean processDeposit(HashMap<String, Object> hashmapParam) {
        return depositMapper.processDeposit(hashmapParam);
    }
}