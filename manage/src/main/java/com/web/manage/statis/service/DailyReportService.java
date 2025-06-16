package com.web.manage.statis.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.statis.mapper.DailyReportMapper;

import ch.qos.logback.classic.Logger;

@Service
public class DailyReportService {
    @Autowired
    private DailyReportMapper dailyReportMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return dailyReportMapper.getQueryTotalCnt();
    }   

    public List<HashMap<String, Object>> getDailySummary(HashMap<String, Object> hashmapParam) {
        return dailyReportMapper.getDailySummary(hashmapParam); 
    }
     
    public HashMap<String, Object> getDailySummaryTotal(HashMap<String, Object> hashmapParam) {
        return dailyReportMapper.getDailySummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getDailyList(HashMap<String, Object> hashmapParam) {
        return dailyReportMapper.getDailyList(hashmapParam); 
    }
     
    public HashMap<String, Object> getDailyListTotal(HashMap<String, Object> hashmapParam) {
        return dailyReportMapper.getDailyListTotal(hashmapParam);
    }

}
