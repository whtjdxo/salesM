package com.web.manage.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.MainService;

import jakarta.servlet.http.HttpSession;


@Controller 
@RequestMapping("/main")
public class MainController {

	@Autowired
	private MainService mainService;

	@RequestMapping(value="")
	public String mainPage() {
		return "pages/main";
	} 

	@RequestMapping(value = "/daySalesSumm", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO getDaySalesSumm(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> data = new HashMap<String, Object>();
		ReturnDataVO result = new ReturnDataVO();
        
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
		hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType()); 

		try {
			data = mainService.getSalesSummary(hashmapParam); 
			result.setResultCode("S000");
			result.setData(data);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result; 
    }
 

	@RequestMapping(value = "/dayDepoSumm", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO getDayDepoSumm(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> data = new HashMap<String, Object>();
		ReturnDataVO result = new ReturnDataVO();
        
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
		hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType()); 

		try {
            data = mainService.getDepositSummary(hashmapParam);             
            result.setResultCode("S000");
			result.setData(data);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
    }

	@RequestMapping(value = "/salesList", method = RequestMethod.POST)
    public @ResponseBody String getSalesList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(); 
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
		hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType()); 
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);  

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = mainService.getDailySummaryList(hashmapParam);
            int records = mainService.getQueryTotalCnt(); 

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list); 

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jString;  
    }

	@RequestMapping(value = "/boardList", method = RequestMethod.POST)
    public @ResponseBody String getBoardList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		 
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
		hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType()); 
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = mainService.getBoardList(hashmapParam);
            int records = mainService.getQueryTotalCnt(); 

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list); 

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jString;  
    }

}
