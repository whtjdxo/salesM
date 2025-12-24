package com.web.manage.api.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.web.manage.api.service.AppApiService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@RestController
@RequestMapping("/app/api/service")
public class AppApiController {
    
    @Autowired
    private AppApiService appApiService;

    @Value("${global.appApiAuthKey}")
	String GSAppApiAuthKey;


    public boolean isValidApiKey(HttpServletRequest request) {
    	String apiKey = request.getParameter("authKey");
    	return GSAppApiAuthKey.equals(apiKey);
    }
    /**
     * 가맹점 정보 조회
     */
    @RequestMapping(value = "/appApiGetStoreInfo.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getStoreInfo(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();        

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        }else {
            String chainNo = request.getParameter("chainNo");      
            try {
                params.put("chainNo", chainNo);
                HashMap<String, Object> data = appApiService.getStoreInfo(params);
                
                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }
        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 일일 정산 요약 정보
     */
    @RequestMapping(value = "/appApiGetDailySummary.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getDailySummary(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schSdt = request.getParameter("schSdt");
            String schEdt = request.getParameter("schEdt");
            try {
                params.put("chainNo", chainNo);
                params.put("schSdt", schSdt);
                params.put("schEdt", schEdt);
                HashMap<String, Object> data = appApiService.getDailySummary(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 일일 매출 정보
     */
    @RequestMapping(value = "/appApiGetDailySalesInfo.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getDailySalesInfo(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schSdt = request.getParameter("schSdt");
            String schEdt = request.getParameter("schEdt");
            String remittGb = request.getParameter("remittGb");
            try {
                params.put("chainNo", chainNo);
                params.put("schSdt", schSdt);
                params.put("schEdt", schEdt);
                params.put("remittGb", remittGb);
                List<HashMap<String, Object>> data = appApiService.getDailySalesInfo(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 일일 정산 차감 정보
     */
    @RequestMapping(value = "/appApiGetDailySettleInfo.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getDailySettleInfo(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schSdt = request.getParameter("schSdt");
            String schEdt = request.getParameter("schEdt");
            String remittGb = request.getParameter("remittGb");
            try {
                params.put("chainNo", chainNo);
                params.put("schSdt", schSdt);
                params.put("schEdt", schEdt);
                params.put("remittGb", remittGb);
                HashMap<String, Object> data = appApiService.getDailySettleInfo(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 미입금 차감 정보
     */
    @RequestMapping(value = "/getUndepoSubList.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getUndepoSubList(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schSdt = request.getParameter("schSdt");
            String schEdt = request.getParameter("schEdt");
            String remittGb = request.getParameter("remittGb");
            try {
                params.put("chainNo", chainNo);
                params.put("schSdt", schSdt);
                params.put("schEdt", schEdt);
                params.put("remittGb", remittGb);
                List<HashMap<String, Object>> data = appApiService.getUndepoSubList(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 차감 내역
     */
    @RequestMapping(value = "/getSubList.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getSubList(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schSdt = request.getParameter("schSdt");
            String schEdt = request.getParameter("schEdt");
            String remittGb = request.getParameter("remittGb");
            try {
                params.put("chainNo", chainNo);
                params.put("schSdt", schSdt);
                params.put("schEdt", schEdt);
                params.put("remittGb", remittGb);
                List<HashMap<String, Object>> data = appApiService.getSubList(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 월간 선정산 요약 정보
     */
    @RequestMapping(value = "/appApiGetMonthlySummary.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getMonthlySummary(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schMonth = request.getParameter("schMonth");
            try {
                params.put("chainNo", chainNo);
                params.put("schMonth", schMonth);
                HashMap<String, Object> data = appApiService.getMonthlySummary(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
    
    /**
     * 월간 선정산 정보 상세 목록
     */
    @RequestMapping(value = "/appApiGetMonthlySalesList.action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<String> getMonthlySalesList(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Gson gson = new Gson();

        boolean apiValid = isValidApiKey(request);
        if (!apiValid) {
            result.put("repCd", "9999");
            result.put("repMsg", "Error: Invalid API Key");
            result.put("repData", null);
        } else {
            String chainNo = request.getParameter("chainNo");
            String schMonth = request.getParameter("schMonth");
            try {
                params.put("chainNo", chainNo);
                params.put("schMonth", schMonth);
                List<HashMap<String, Object>> data = appApiService.getMonthlySalesList(params);

                result.put("repCd", "0000");
                result.put("repMsg", "Success");
                result.put("repData", data);
            } catch (Exception e) {
                result.put("repCd", "9999");
                result.put("repMsg", "Error: " + e.getMessage());
                result.put("repData", null);
            }
        }

        String jsonResponse = gson.toJson(result);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(jsonResponse);
    }
}
