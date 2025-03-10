package com.web.manage.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.base.service.AgencyService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.base.domain.AgencyVO;
import jakarta.validation.Valid;

import com.web.common.util.DateUtil;
import com.web.common.util.StringUtil;
import com.web.common.util.ValidateUtil;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/base/organ/agencyMng")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @RequestMapping("view")
    public String view() {
        return "pages/base/agencyMng";
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

            list = agencyService.getAgencyList(hashmapParam);
            int records = agencyService.getQueryTotalCnt();

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

    @RequestMapping(value = "/ceoIdDupChk", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO ceoIdDupChk(@RequestBody String ceo_id) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (agencyService.getCeoIdDupChk(ceo_id) == 0) {
                result.setResultCode("S000");
                result.setResultMsg("Available CEO ID.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("Duplicate CEO ID.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Credit creation failed.");
            e.printStackTrace();
        }        
        return result;
    }
    
    @RequestMapping(value = "/agencyCreate", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO agencyCreate(@ModelAttribute("AgencyVO") @Valid AgencyVO agencyVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            String agency_no = agencyService.createAgencyNo();
			agencyVO.setAgency_no(agency_no);

            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    agencyVO.setEnt_user_id(member.getUserId());

            if (agencyService.agencyCreate(agencyVO)) {
                System.out.println("agencyCreate success");
                result.setResultCode("S000");
                result.setResultMsg("Agency creation successful.");
            } else {
                System.out.println("agencyCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Agency creation failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Agency creation failed.");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/agencyUpdate", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO agencyUpdate(@ModelAttribute("AgencyVO") @Valid AgencyVO agencyVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    agencyVO.setUpt_user_id(member.getUserId());
            if (agencyService.agencyUpdate(agencyVO)) {
                System.out.println("agencyUpdate  success");
                result.setResultCode("S000");
                result.setResultMsg("Agency update successful.");
            } else {
                System.out.println("agencyUpdate  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Agency update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Agency update failed.");
            e.printStackTrace();
        }
        return result;
    }
}
 
