package com.web.manage.api.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.api.domain.ScrapCompVO;
import com.web.manage.api.domain.ScrapDeliDataVO;
import com.web.manage.api.domain.ScrapErrorLogVO;
import com.web.manage.api.domain.ScrapLogVO;
import com.web.manage.api.domain.ScrapUserVO;
import com.web.manage.api.domain.ScrapVanDataVO;
import com.web.manage.api.mapper.ScrapMapper;

import ch.qos.logback.classic.Logger; 

@Service
public class ScrapService {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private ScrapMapper scrapMapper;

    public String getUserAuthKey(ScrapUserVO scrapUserVo) {
        String usrAuthKey = "";
        if (scrapMapper.getUserCheck(scrapUserVo) > 0){
            String currentDateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            usrAuthKey = java.util.Base64.getEncoder().encodeToString((scrapUserVo.getUserId() + currentDateTime).getBytes());
            scrapUserVo.setUserAuthKey(usrAuthKey);
            if(scrapMapper.setUserAuthKey(scrapUserVo)== false){
                usrAuthKey = "";
            }
        }
        return usrAuthKey;
    }

    public int getUserAuthKeyCheck(ScrapUserVO scrapUserVo) {
        return scrapMapper.getUserAuthKeyCheck(scrapUserVo) ;
    }

    public List<HashMap<String, Object>> getVanChainList(ScrapCompVO scrapCompVO) {
        return scrapMapper.getVanChainList(scrapCompVO);
    }
     
    @Transactional
    public boolean scrapUploadVanData(String uploadData, ScrapLogVO logVO) {       
        // ScrapVanDataVO scrapVanDataVO = new ScrapVanDataVO();
        int totDataCnt = 0;
		int succDataCnt  = 0;
		int dupDataCnt   = 0;
        try {
            // Parse JSON and populate scrapVanDataVO
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            List<ScrapVanDataVO> vanDataList = objectMapper.readValue(uploadData, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ScrapVanDataVO.class)
                    ); 
            for (ScrapVanDataVO vanData : vanDataList) {
                totDataCnt += 1;
                try {
                    // System.out.println(vanData);
                    scrapMapper.scrapUploadVanData(vanData);
                    succDataCnt += 1;
                } catch (org.springframework.dao.DuplicateKeyException e) {
                    // Ignore duplicate key errors and continue processing
                    dupDataCnt += 1;
                    continue;
                } catch (Exception e) {
                    // Rollback for other errors
                    throw new RuntimeException("Error during scrapUploadVanData", e);
                }
            }

            String rsltMsg = "";
            dupDataCnt = totDataCnt - succDataCnt;
            rsltMsg = "Upload : " + totDataCnt + " Save : " + succDataCnt + " Dup : " + dupDataCnt;
            
            logVO.setUpload_cnt(String.valueOf(totDataCnt));
            logVO.setDup_cnt(String.valueOf(dupDataCnt));
            logVO.setSave_cnt(String.valueOf(succDataCnt));
            logVO.setRslt_msg(rsltMsg); 
            
            // 데이터 처리 결과 insert
            try {
                scrapMapper.writeScrapLog(logVO);
                logger.debug(">> " + rsltMsg);
            } catch (Exception e) {
                // Log the exception or handle it appropriately
                logger.debug(">> " + "Error writing scrap log: " + e.getMessage());
                // System.err.println("Error writing scrap log: " + e.getMessage());
            } 
            return true;

        } catch (Exception e) {
            // Rollback for other errors
            throw new RuntimeException("Error during scrapUploadVanData", e);
        }
    }

    @Transactional
    public boolean scrapUploadDeliData(String uploadData, ScrapLogVO logVO) {       
        int totDataCnt = 0;
		int succDataCnt  = 0;
		int dupDataCnt   = 0;
        try {
            // Parse JSON and populate scrapVanDataVO
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            List<ScrapDeliDataVO> deliDataList = objectMapper.readValue(uploadData, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ScrapDeliDataVO.class)
                    ); 
            for (ScrapDeliDataVO deliData : deliDataList) {
                totDataCnt += 1;
                try {
                    // System.out.println(vanData);
                    scrapMapper.scrapUploadDeliData(deliData);
                    succDataCnt += 1;
                } catch (org.springframework.dao.DuplicateKeyException e) {
                    // Ignore duplicate key errors and continue processing
                    dupDataCnt += 1;
                    continue;
                } catch (Exception e) {
                    // Rollback for other errors
                    throw new RuntimeException("Error during scrapUploadVanData", e);
                }
            }

            String rsltMsg = "";
            dupDataCnt = totDataCnt - succDataCnt;
            rsltMsg = "Upload : " + totDataCnt + " Save : " + succDataCnt + " Dup : " + dupDataCnt;
            
            logVO.setUpload_cnt(String.valueOf(totDataCnt));
            logVO.setDup_cnt(String.valueOf(dupDataCnt));
            logVO.setSave_cnt(String.valueOf(succDataCnt));
            logVO.setRslt_msg(rsltMsg); 
            
            // 데이터 처리 결과 insert
            try {
                scrapMapper.writeScrapLog(logVO);
            } catch (Exception e) {
                // Log the exception or handle it appropriately
                System.err.println("Error writing scrap log: " + e.getMessage());
            } 
            return true;

        } catch (Exception e) {
            // Rollback for other errors
            throw new RuntimeException("Error during scrapUploadVanData", e);
        }
    }


    public boolean writeScrapErrorLog(ScrapErrorLogVO errLogVo) {       
        return scrapMapper.writeScrapErrorLog(errLogVo);
               
    }
 
}
