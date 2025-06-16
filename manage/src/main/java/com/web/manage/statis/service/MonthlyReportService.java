package com.web.manage.statis.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.statis.mapper.MonthlyReportMapper;

import ch.qos.logback.classic.Logger;

@Service
public class MonthlyReportService {
    @Autowired
    private MonthlyReportMapper monthlyReportMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    public int getQueryTotalCnt() {
        return monthlyReportMapper.getQueryTotalCnt();
    }   

    public List<HashMap<String, Object>> getMonthlySummary(HashMap<String, Object> hashmapParam) {
        return monthlyReportMapper.getMonthlySummary(hashmapParam); 
    }
     
    public HashMap<String, Object> getMonthlySummaryTotal(HashMap<String, Object> hashmapParam) {
        return monthlyReportMapper.getMonthlySummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getMonthlyList(HashMap<String, Object> hashmapParam) {
        return monthlyReportMapper.getMonthlyList(hashmapParam); 
    }
     
    public HashMap<String, Object> getMonthlyListTotal(HashMap<String, Object> hashmapParam) {
        return monthlyReportMapper.getMonthlyListTotal(hashmapParam);
    }

}
