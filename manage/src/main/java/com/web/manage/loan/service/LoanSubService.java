package com.web.manage.loan.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.loan.mapper.LoanSubMapper;

import ch.qos.logback.classic.Logger;

@Service
public class LoanSubService {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private LoanSubMapper loanSubMapper;

    public int getQueryTotalCnt() {
        return loanSubMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getLoanSubSummary(HashMap<String, Object> hashmapParam) {
        return loanSubMapper.getLoanSubSummary(hashmapParam);
    }

    public HashMap<String, Object> getLoanSubSummaryTotal(HashMap<String, Object> hashmapParam) {
        return loanSubMapper.getLoanSubSummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainLoanSubList(HashMap<String, Object> hashmapParam) {    
        return loanSubMapper.getChainLoanSubList(hashmapParam);
    }

    public HashMap<String, Object> getChainLoanSubListTotal(HashMap<String, Object> hashmapParam) {
        return loanSubMapper.getChainLoanSubListTotal(hashmapParam);
    }
}
