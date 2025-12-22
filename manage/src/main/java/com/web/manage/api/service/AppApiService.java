package com.web.manage.api.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.api.mapper.AppApiMapper;

import ch.qos.logback.classic.Logger;

@Service
public class AppApiService {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AppApiMapper appApiMapper;

    /**
     * 가맹점 정보 조회
     */
    public HashMap<String, Object> getStoreInfo(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetStoreInfo(params);
        } catch (Exception e) {
            logger.error("Error fetching store info: ", e);
            return null;
        }
    }

    /**
     * 일일 정산 요약 정보
     */
    public HashMap<String, Object> getDailySummary(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetDailySummary(params);
        } catch (Exception e) {
            logger.error("Error fetching daily summary: ", e);
            return null;
        }
    }

    /**
     * 일일 매출 정보
     */
    public List<HashMap<String, Object>> getDailySalesInfo(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetDailySalesInfo(params);
        } catch (Exception e) {
            logger.error("Error fetching daily sales info: ", e);
            return null;
        }
    }

    /**
     * 일일 정산 차감 정보
     */
    public HashMap<String, Object> getDailySettleInfo(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetDailySettleInfo(params);
        } catch (Exception e) {
            logger.error("Error fetching daily settle info: ", e);
            return null;
        }
    }

    /**
     * 미입금 차감 정보
     */
    public List<HashMap<String, Object>> getUndepoSubList(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetUndepoSubList(params);
        } catch (Exception e) {
            logger.error("Error fetching undepo sub list: ", e);
            return null;
        }
    }

    /**
     * 차감 내역
     */
    public List<HashMap<String, Object>> getSubList(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetSubList(params);
        } catch (Exception e) {
            logger.error("Error fetching sub list: ", e);
            return null;
        }
    }

    /**
     * 월간 선정산 요약 정보
     */
    public HashMap<String, Object> getMonthlySummary(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetMonthlySummary(params);
        } catch (Exception e) {
            logger.error("Error fetching monthly summary: ", e);
            return null;
        }
    }

    /**
     * 월간 선정산 정보 상세 목록
     */
    public List<HashMap<String, Object>> getMonthlySalesList(HashMap<String, Object> params) {
        try {
            return appApiMapper.appApiGetMonthlySalesList(params);
        } catch (Exception e) {
            logger.error("Error fetching monthly sales list: ", e);
            return null;
        }
    }
}
