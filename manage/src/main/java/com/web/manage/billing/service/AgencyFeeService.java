package com.web.manage.billing.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.billing.mapper.AgencyFeeMapper;

import ch.qos.logback.classic.Logger;

@Service
public class AgencyFeeService {
    @Autowired
    private AgencyFeeMapper agencyFeeMapper;

    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return agencyFeeMapper.getQueryTotalCnt();
    }   

    public List<HashMap<String, Object>> getAgencyFeeSummary(HashMap<String, Object> hashmapParam) {
        return agencyFeeMapper.getAgencyFeeSummary(hashmapParam); 
    }
     
    public HashMap<String, Object> getAgencyFeeSummaryTotal(HashMap<String, Object> hashmapParam) {
        return agencyFeeMapper.getAgencyFeeSummaryTotal(hashmapParam);
    } 


    public List<HashMap<String, Object>> getAgencyFeeList(HashMap<String, Object> hashmapParam) {
        return agencyFeeMapper.getAgencyFeeList(hashmapParam); 
    }
     
    public HashMap<String, Object> getAgencyFeeListTotal(HashMap<String, Object> hashmapParam) {
        return agencyFeeMapper.getAgencyFeeListTotal(hashmapParam);
    }
}
