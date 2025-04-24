package com.web.manage.api.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.web.common.util.StringUtil;
import com.web.manage.api.domain.ScrapCompVO;
import com.web.manage.api.domain.ScrapErrorLogVO;
import com.web.manage.api.domain.ScrapLogVO;
import com.web.manage.api.domain.ScrapUserVO;
import com.web.manage.api.domain.ScrapVanDataVO;
import com.web.manage.api.service.ScrapService;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

@Controller
@RestController
@RequestMapping("/api/scrap")
public class ScrapController {
    @Autowired
    private ScrapService scrapService;
  
    // @RequestMapping("/scrapLogin")    
    // public @ResponseBody String scrapLogin(@RequestBody HashMap<String, Object> hashmapParam) {  
    @RequestMapping(value = "/scrapLogin.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> scrapLogin( @RequestParam("userId") String userId
                                            , @RequestParam("userPwd") String userPwd
                                            , @RequestParam("authKey") String authKey) {        
        
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO(); 
        
        System.out.println("userId : " + userId);
        System.out.println("userPwd : " + userPwd);

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserPwd(userPwd);
          
        Gson gson = new Gson();
        String jString = null;
        try {         
            String apiAuthKey = scrapService.getUserAuthKey(scrapUserVo);  
            if  ("".equals(apiAuthKey)) {
                hashmapResult.put("repCd", "9999");
                hashmapResult.put("repMsg", "User Check Fail~");
                hashmapResult.put("apiAuthKey", apiAuthKey);
            } else {
                hashmapResult.put("repCd", "0000");
                hashmapResult.put("repMsg", "User Check Complete~");
                hashmapResult.put("apiAuthKey", apiAuthKey);                
            }
            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }       

        System.out.println(jString);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }

    @RequestMapping(value = "/getScrapVanCompList.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getVanChainList(@RequestParam("vanCd") String vanCd
                                                , @RequestParam("chainNo") String chainNo
                                                , @RequestParam("userId") String userId
                                                , @RequestParam("apiAuthKey") String apiAuthKey
                                                , @RequestParam("authKey") String authKey
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapCompVO scrapCompVO = new ScrapCompVO(); 
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey); 

		if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
            hashmapResult.put("repCd", "9001");
            hashmapResult.put("repMsg", "User AuthKey Check Fail~");
            hashmapResult.put("apiAuthKey", ""); 
            jString = gson.toJson(hashmapResult);
            return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);                  
        } 

        scrapCompVO.setVanCd(vanCd);
        scrapCompVO.setChainNo(chainNo);
        scrapCompVO.setUserId(userId);
        scrapCompVO.setApiAuthKey(apiAuthKey);
        scrapCompVO.setAuthKey(authKey); 
        
        try {
            resultList = scrapService.getVanChainList(scrapCompVO);
            
            hashmapResult.put("repCd", "0000");
            hashmapResult.put("repMsg", "Get Data List"); 
            hashmapResult.put("repData", resultList);
            System.out.println("resultList : " + resultList);
            // hashmapResult.put("repData", gson.toJson(list));            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9100");
            hashmapResult.put("repMsg", "Get Data List Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }

    @RequestMapping(value = "/getScrapBankList.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getScrapBankCompList(@RequestParam("bankCd") String bankCd
                                                , @RequestParam("chainNo") String chainNo
                                                , @RequestParam("userId") String userId
                                                , @RequestParam("apiAuthKey") String apiAuthKey
                                                , @RequestParam("authKey") String authKey
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapCompVO scrapCompVO = new ScrapCompVO(); 
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey); 

		if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
            hashmapResult.put("repCd", "9001");
            hashmapResult.put("repMsg", "User AuthKey Check Fail~");
            hashmapResult.put("apiAuthKey", ""); 
            jString = gson.toJson(hashmapResult);
            return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);                      
        } 

        scrapCompVO.setBankCd(bankCd);
        scrapCompVO.setChainNo(chainNo);
        scrapCompVO.setUserId(userId);
        scrapCompVO.setApiAuthKey(apiAuthKey);
        scrapCompVO.setAuthKey(authKey); 
        
        try {
            resultList = scrapService.getBankChainList(scrapCompVO);
            
            hashmapResult.put("repCd", "0000");
            hashmapResult.put("repMsg", "Get Data List"); 
            hashmapResult.put("repData", resultList);
            System.out.println("resultList : " + resultList);
            // hashmapResult.put("repData", gson.toJson(list));            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9100");
            hashmapResult.put("repMsg", "Get Data List Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }


    @RequestMapping(value = "/scrapApiUploadVanData.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> scrapUploadVanData(@RequestParam("vanCd") String vanCd
                                                    , @RequestParam("chainNo") String chainNo
                                                    , @RequestParam("userId") String userId
                                                    , @RequestParam("apiAuthKey") String apiAuthKey
                                                    , @RequestParam("authKey") String authKey
                                                    , @RequestParam("uploadData") String uploadData
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapLogVO logVo = new ScrapLogVO();

        // ScrapVanDataVO  scrapVanDataVO = new ScrapVanDataVO(); 

        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey); 
        

		if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
            hashmapResult.put("repCd", "9001");
            hashmapResult.put("repMsg", "User AuthKey Check Fail~");
            hashmapResult.put("apiAuthKey", ""); 
            jString = gson.toJson(hashmapResult);
            return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);                
        }  
        try {
            logVo.setChain_no(chainNo);
            logVo.setVan_cd(vanCd);
            logVo.setScrap_gb("VAN");
            // System.out.println(uploadData);

            if ( scrapService.scrapUploadVanData(uploadData, logVo)) {
                hashmapResult.put("repCd", "0000");
                hashmapResult.put("repMsg", "Data Upload Complete"); 
                hashmapResult.put("repData", "");
            } else {
                hashmapResult.put("repCd", "9100");
                hashmapResult.put("repMsg", "Insert Data Fail"); 
                hashmapResult.put("repData", "");
            }            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9900");
            hashmapResult.put("repMsg", "Insert Data Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }


    @RequestMapping(value = "/scrapApiUploadDeliveryData.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> scrapUploadDeliveryData(@RequestParam("vanCd") String vanCd
                                                    , @RequestParam("chainNo") String chainNo
                                                    , @RequestParam("userId") String userId
                                                    , @RequestParam("apiAuthKey") String apiAuthKey
                                                    , @RequestParam("authKey") String authKey
                                                    , @RequestParam("uploadData") String uploadData
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapLogVO logVo = new ScrapLogVO();

        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey);         

		if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
            hashmapResult.put("repCd", "9001");
            hashmapResult.put("repMsg", "User AuthKey Check Fail~");
            hashmapResult.put("apiAuthKey", ""); 
            jString = gson.toJson(hashmapResult);
            return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);                  
        }  
        try {
            logVo.setChain_no(chainNo);
            logVo.setVan_cd(vanCd);
            logVo.setScrap_gb("DELI");
            // System.out.println(uploadData);

            if ( scrapService.scrapUploadDeliData(uploadData, logVo)) {
                hashmapResult.put("repCd", "0000");
                hashmapResult.put("repMsg", "Data Upload Complete"); 
                hashmapResult.put("repData", "");
            } else {
                hashmapResult.put("repCd", "9100");
                hashmapResult.put("repMsg", "Insert Data Fail"); 
                hashmapResult.put("repData", "");
            }            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9900");
            hashmapResult.put("repMsg", "Insert Data Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);


        // return jString;  
    }

    @RequestMapping(value = "/scrapApiUploadBankData.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> scrapApiUploadBankData(@RequestParam("bankCd") String bankCd
                                                    , @RequestParam("chainNo") String chainNo
                                                    , @RequestParam("userId") String userId
                                                    , @RequestParam("apiAuthKey") String apiAuthKey
                                                    , @RequestParam("authKey") String authKey
                                                    , @RequestParam("uploadData") String uploadData
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapLogVO logVo = new ScrapLogVO();

        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크

        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey);         

		if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
            hashmapResult.put("repCd", "9001");
            hashmapResult.put("repMsg", "User AuthKey Check Fail~");
            hashmapResult.put("apiAuthKey", ""); 
            jString = gson.toJson(hashmapResult);
            return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);                    
        }  
        try {
            logVo.setChain_no(chainNo);
            logVo.setVan_cd(bankCd);
            logVo.setScrap_gb("BANK");
            System.out.println(uploadData);

            if ( scrapService.scrapUploadBankData(uploadData, logVo)) {
                hashmapResult.put("repCd", "0000");
                hashmapResult.put("repMsg", "Data Upload Complete"); 
                hashmapResult.put("repData", "");
            } else {
                hashmapResult.put("repCd", "9100");
                hashmapResult.put("repMsg", "Insert Data Fail"); 
                hashmapResult.put("repData", "");
            }            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9900");
            hashmapResult.put("repMsg", "Insert Data Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }

    @RequestMapping(value = "/scrapApiWriteErrorLog.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> writeScrapErrorLog(@RequestParam("userId") String userId
                                                    , @RequestParam("apiAuthKey") String apiAuthKey
                                                    , @RequestParam("authKey") String authKey
                                                    , @RequestParam("vanCd") String vanCd
                                                    , @RequestParam("chainNo") String chainNo
                                                    , @RequestParam("chainNm") String chainNm
                                                    , @RequestParam("loginId") String loginId
                                                    , @RequestParam("loginPwd") String loginPwd
                                                    , @RequestParam("errorMsg") String errorMsg
                                                ) {         		
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>(); 
        ScrapUserVO scrapUserVo = new ScrapUserVO();
        ScrapErrorLogVO  errorLogVo = new ScrapErrorLogVO(); 

        Gson gson = new Gson();
        String jString = null;
        // -- 사용자 인증키 체크
        scrapUserVo.setUserId(userId);
        scrapUserVo.setUserAuthKey(apiAuthKey); 
		// if (scrapService.getUserAuthKeyCheck(scrapUserVo) <= 0){
        //     hashmapResult.put("repCd", "9001");
        //     hashmapResult.put("repMsg", "User AuthKey Check Fail~");
        //     hashmapResult.put("apiAuthKey", ""); 
        //     jString = gson.toJson(hashmapResult);
        //     return jString;                       
        // }
        errorLogVo.setUserId(userId);
        errorLogVo.setApiAuthKey(apiAuthKey);
        errorLogVo.setAuthKey(authKey);
        errorLogVo.setVanCd(vanCd);
        errorLogVo.setChainNo(chainNo);
        errorLogVo.setChainNm(chainNm);
        errorLogVo.setLoginId(loginId);
        errorLogVo.setLoginPwd(loginPwd);
        errorLogVo.setErrorMsg(errorMsg);

        try {
            if ( scrapService.writeScrapErrorLog(errorLogVo)) {
                hashmapResult.put("repCd", "0000");
                hashmapResult.put("repMsg", "Error Log Write Complete"); 
                hashmapResult.put("repData", "");
            } else {
                hashmapResult.put("repCd", "9100");
                hashmapResult.put("repMsg", "Insert Log Write Fail"); 
                hashmapResult.put("repData", null);
            }            
        } catch (Exception e) {
            e.printStackTrace();
            hashmapResult.put("repCd", "9900");
            hashmapResult.put("repMsg", "Insert Log Write Fail"); 
            hashmapResult.put("repData", null);
        }

        jString = gson.toJson(hashmapResult);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jString);
    }
 
}
