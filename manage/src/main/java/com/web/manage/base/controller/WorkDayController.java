package com.web.manage.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.manage.base.domain.WorkDayVO;
import com.web.manage.base.service.WorkDayService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/base/organ/workDayMng")
public class WorkDayController {

    @Autowired
    private WorkDayService workDayService;

    @RequestMapping("view")
    public String view() {
        return "pages/base/workDayMng";
    }

    @RequestMapping("monthDay")    
    public @ResponseBody String getMonthDayList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {        
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null;
        try {
            // System.out.println("sch_mon : " + hashmapParam.get("sch_mon").toString());
            list = workDayService.getMonthDayList(hashmapParam);            
            hashmapResult.put("data", list);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return jString; 
    } 

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam) {    
        HashMap<String, Object> hashmapResult = new HashMap<>();        
        Gson gson = new Gson();
        String jString = null;
        try {
            List<HashMap<String, Object>> list = workDayService.getWorkDayList(hashmapParam);
            hashmapResult.put("data", list);
            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(jString);
        return jString;
        
    }

    @RequestMapping(value = "/dayInfo", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public @ResponseBody String getDayInfo(@RequestBody HashMap<String, Object> hashmapParam) {    
        HashMap<String, Object> hashmapResult = new HashMap<>();        
        Gson gson = new Gson();
        String jString = null;
        try {
            HashMap<String, Object> dayInfo = workDayService.getDayInfo(hashmapParam);
            hashmapResult.put("data", dayInfo);
            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(jString);
        return jString;
        
    }

    @RequestMapping(value = "/insertWorkDay", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO insertWorkDay(@ModelAttribute("WorkDayVO") @Valid WorkDayVO workDayInfo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();         
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        workDayInfo.setUpt_user_id(member.getUserId()); 
        // ReturnDataVO result = new ReturnDataVO(); 
        // WorkDayVO workDayInfo = new WorkDayVO();       
        // workDayInfo.setWork_date(hashmapParam.get("work_date").toString());
        // workDayInfo.setWorking(hashmapParam.get("working").toString());                
        // SessionVO member = (SessionVO) session.getAttribute("S_USER");
        // workDayInfo.setUpt_user_id(member.getUserId()); 
        System.out.println(workDayInfo);
        try {
            if (workDayService.insertWorkDay(workDayInfo)) {
                result.setResultCode("S000");
                result.setResultMsg("WorkDay creation successful.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("WorkDay creation failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("WorkDay creation failed.");
            e.printStackTrace();
        }
        return result;
    }
}
