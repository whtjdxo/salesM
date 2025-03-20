package com.web.manage.base.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.manage.base.service.WorkDayService;
import com.web.manage.common.domain.ReturnDataVO;

@Controller
@RequestMapping("/base/organ/workDayMng")
public class WorkDayController {

    @Autowired
    private WorkDayService workDayService;

    @RequestMapping("view")
    public String view() {
        return "pages/base/workDayMng";
    }

    @RequestMapping("list")
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> hashmapResult = new HashMap<>();
        Gson gson = new Gson();
        try {
            List<HashMap<String, Object>> list = workDayService.getWorkDayList(hashmapParam);
            hashmapResult.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(hashmapResult);
    }

    @RequestMapping(value = "/insertWorkDay", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO insertWorkDay(@RequestBody HashMap<String, Object> workDayData) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (workDayService.insertWorkDay(workDayData)) {
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
