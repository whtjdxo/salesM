package com.web.manage.api.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.manage.api.domain.ScrapCompVO;
import com.web.manage.api.domain.ScrapErrorLogVO;
import com.web.manage.api.domain.ScrapUserVO;
import com.web.manage.api.domain.ScrapVanDataVO;
import com.web.manage.api.mapper.ScrapMapper; 

@Service
public class ScrapService {
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
    public boolean scrapUploadVanData(String uploadData) {       
        // ScrapVanDataVO scrapVanDataVO = new ScrapVanDataVO();
        try {
            // Parse JSON and populate scrapVanDataVO
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            System.out.println("vanDataList Mapping ==========================================");
            List<ScrapVanDataVO> vanDataList = objectMapper.readValue(uploadData, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ScrapVanDataVO.class)
                    );
            System.out.println("vanDataList Mapping ==========================================");
            
            for (ScrapVanDataVO vanData : vanDataList) {
                try {
                    System.out.println(vanData);
                    scrapMapper.scrapUploadVanData(vanData);
                } catch (org.springframework.dao.DuplicateKeyException e) {
                    // Ignore duplicate key errors and continue processing
                    continue;
                } catch (Exception e) {
                    // Rollback for other errors
                    throw new RuntimeException("Error during scrapUploadVanData", e);
                }
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
