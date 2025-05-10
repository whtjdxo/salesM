package com.web.manage.deposit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.web.manage.deposit.domain.ExceedMstVO;
import com.web.manage.deposit.service.ExceedService;
import com.web.manage.user.domain.UserVO;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/deposit/exceed/")
public class ExceedController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    @Autowired
    private ExceedService exceedService;
    
    @Autowired
    private CommonService commonService; 

    @RequestMapping("excMng/view")
    public String view() {
        return "pages/deposit/exceedMng";
    }
 
    @RequestMapping("excMng/excSummary")    
    public @ResponseBody String getExcSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm     = new HashMap<String, Object>();
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

            list = exceedService.getExcSummary(hashmapParam);
            int records = exceedService.getQueryTotalCnt();
            totalSumm = exceedService.getExcSummTotal(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("totalSumm", totalSumm);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping("excMng/chainExcList")    
    public @ResponseBody String getChainExcList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm     = new HashMap<String, Object>();
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

            list = exceedService.getChainExcList(hashmapParam);
            int records = exceedService.getQueryTotalCnt();
            totalSumm = exceedService.getChainExcListTotal(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("totalSumm", totalSumm);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }
    
    @RequestMapping(value = "excMng/insertExceedMst", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertExceedMst(@ModelAttribute("ExceedMstVO") @Valid ExceedMstVO exceedMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {            
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    exceedMstVo.setEnt_user_id(member.getUserId());

            exceedMstVo.setOccur_amt(exceedMstVo.getOccur_amt().replace(",", "") );
            exceedMstVo.setIssue_amt(exceedMstVo.getIssue_amt().replace(",", "") );
            exceedMstVo.setRemain_amt(exceedMstVo.getRemain_amt().replace(",", "") );

            if (exceedService.insertExceedMst(exceedMstVo)) {
                System.out.println("Exceed Amt Create success");
                result.setResultCode("S000");
                result.setResultMsg("Exceed Amt creation successful.");
            } else {
                System.out.println("Exceed Amt fail");
                result.setResultCode("F000");
                result.setResultMsg("Exceed Amt creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Exceed Amt creation Failed");
            e.printStackTrace();
        }
        return result;
    }
 

    @RequestMapping(value = "excMng/updateExceedMst", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateExceedMst(@ModelAttribute("ExceedMstVO") @Valid ExceedMstVO exceedMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    exceedMstVo.setUpt_user_id(member.getUserId()); 
            exceedMstVo.setOccur_amt(exceedMstVo.getOccur_amt().replace(",", "") );
            exceedMstVo.setIssue_amt(exceedMstVo.getIssue_amt().replace(",", "") );
            exceedMstVo.setRemain_amt(exceedMstVo.getRemain_amt().replace(",", "") );

            if (exceedService.updateExceedMst(exceedMstVo)) {
                System.out.println("Update Exceed  success");
                result.setResultCode("S000");
                result.setResultMsg("Exceed Update successful.");     
                
            } else {
                System.out.println("UpdateExceed  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Exceed update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Exceed update failed.");
            e.printStackTrace();
        }
        return result;
    }
}
