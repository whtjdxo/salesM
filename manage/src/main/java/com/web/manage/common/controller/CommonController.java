package com.web.manage.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.service.CommonService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/common/")
public class CommonController {
    
    @Autowired
    private CommonService commonService;

    /**
	 * 화면에 쓸 전체 코드 목록 가져오기
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/totalCodelist")
	public @ResponseBody ReturnDataVO totalCodelist(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		try {
			list = commonService.getTotalCodelist(hashmapParam);
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="getCreditCorpList")
	public @ResponseBody ReturnDataVO getCreditCorpList(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		try {
			list = commonService.getCreditCorpList(hashmapParam);
			
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/getChainList")
	public @ResponseBody ReturnDataVO getChainList(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		System.out.println("hashmapParam : " + hashmapParam);
		try {
			list = commonService.getChainList(hashmapParam);
			
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/getLinkChainList")
	public @ResponseBody String getLinkChainList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
            
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = commonService.getLinkChainList(hashmapParam);
			
            int records = commonService.getQueryTotalCnt();

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

	@RequestMapping(value="/getShiftChainList")
	public @ResponseBody ReturnDataVO getShiftChainList(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		System.out.println("hashmapParam : " + hashmapParam);
		try {
			list = commonService.getShiftChainList(hashmapParam);
			
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/getAgencyList")
	public @ResponseBody ReturnDataVO getAgencyList(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		try {
			list = commonService.getAgencyList(hashmapParam);
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}


	@RequestMapping(value="/getPreWorkDay")
	public @ResponseBody ReturnDataVO getPreWorkDay(@RequestParam HashMap<String, String> hashmapParam){
		String nDate ="";
		ReturnDataVO result = new ReturnDataVO();
		try {
			nDate = commonService.getPreWorkDay();
			result.setResultCode("S000");
			result.setData(nDate);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/getPreNextDay")
	public @ResponseBody ReturnDataVO getPreNextDay(@RequestParam HashMap<String, String> hashmapParam){
		String nDate ="";
		ReturnDataVO result = new ReturnDataVO();
		try {
			nDate = commonService.getNextWorkDay();
			result.setResultCode("S000");
			result.setData(nDate);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/getToDay")
	public @ResponseBody ReturnDataVO getToDay(@RequestParam HashMap<String, String> hashmapParam){
		String nDate ="";
		ReturnDataVO result = new ReturnDataVO();
		try {
			nDate = commonService.getToDay();
			result.setResultCode("S000");
			result.setData(nDate);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}
	 
}
