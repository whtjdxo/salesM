package com.web.manage.statis.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.statis.mapper.SalesReportMapper;

import ch.qos.logback.classic.Logger;

@Service
public class SalesReportService {
    @Autowired
    private SalesReportMapper salesReportMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return salesReportMapper.getQueryTotalCnt();
    }   

    public List<HashMap<String, Object>> getSalesSummary(HashMap<String, Object> hashmapParam) {
        return salesReportMapper.getSalesSummary(hashmapParam); 
    }
     
    public HashMap<String, Object> getSalesSummaryTotal(HashMap<String, Object> hashmapParam) {
        return salesReportMapper.getSalesSummaryTotal(hashmapParam);
    } 

    public List<HashMap<String, Object>> getSalesTransition(HashMap<String, Object> hashmapParam) {
        return salesReportMapper.getSalesTransition(hashmapParam); 
    }

    public HashMap<String, Object> getChainSalesTransition(HashMap<String, Object> hashmapParam) {
        return salesReportMapper.getChainSalesTransition(hashmapParam);
    }
     
    public HashMap<String, Object> getSalesTransitionTotal(HashMap<String, Object> hashmapParam) {
        return salesReportMapper.getSalesTransitionTotal(hashmapParam);
    }
}
