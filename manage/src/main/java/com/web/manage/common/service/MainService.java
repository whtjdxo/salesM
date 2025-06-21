package com.web.manage.common.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.mapper.MainMapper;

import ch.qos.logback.classic.Logger;


@Service
public class MainService {

	@Autowired
	private MainMapper mainMapper;

	static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return mainMapper.getQueryTotalCnt();
    }    
    
     
    public HashMap<String, Object> getSalesSummary(HashMap<String, Object> hashmapParam) {
        return mainMapper.getSalesSummary(hashmapParam);
    } 

    public HashMap<String, Object> getDepositSummary(HashMap<String, Object> hashmapParam) {
        return mainMapper.getDepositSummary(hashmapParam);
    } 

    public List<HashMap<String, Object>> getDailySummaryList(HashMap<String, Object> hashmapParam) {
        return mainMapper.getDailySummaryList(hashmapParam); 
    }

    public List<HashMap<String, Object>> getBoardList(HashMap<String, Object> hashmapParam) {
        return mainMapper.getBoardList(hashmapParam); 
    }

}
