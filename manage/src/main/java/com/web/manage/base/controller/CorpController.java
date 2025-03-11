package com.web.manage.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.web.common.util.StringUtil;
import com.web.manage.base.service.CorpService; 
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO; 
import com.web.manage.base.domain.CorpVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/base/organ/corpMng")
public class CorpController {
    CorpVO corpVO = new CorpVO();
    @Autowired
    private CorpService corpService;

    @RequestMapping("view")
    public String view() {
        return "pages/base/corpMng";
    }

    @RequestMapping("list")    
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {        
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            // System.out.println("hashmapParam : " + hashmapParam);

            list = corpService.getCorpList(hashmapParam);
            int records = corpService.getQueryTotalCnt();

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
    
    @RequestMapping(value = "/corpCreate", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO corpCreate(@ModelAttribute("CorpVo") @Valid CorpVO corpVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            String corp_cd = corpService.createCorpCd();
			corpVO.setCorp_cd(corp_cd);

            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    corpVO.setEnt_user_id(member.getUserId());

            if (corpService.corpCreate(corpVO)) {
                System.out.println("corpCreate success");
                result.setResultCode("S000");
                result.setResultMsg("corp creation successful.");
            } else {
                System.out.println("corpCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("corp creation failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("corp creation failed.");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/corpUpdate", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO corpUpdate(@ModelAttribute("CorpVo") @Valid CorpVO corpVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    corpVO.setUpt_user_id(member.getUserId());
            if (corpService.corpUpdate(corpVO)) {
                System.out.println("corpUpdate  success");
                result.setResultCode("S000");
                result.setResultMsg("corp update successful.");
            } else {
                System.out.println("corpUpdate  Fail");
                result.setResultCode("F000");
                result.setResultMsg("corp update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("corp update failed.");
            e.printStackTrace();
        }
        return result;
    }
}
