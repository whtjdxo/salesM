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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.base.service.AgencyService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
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
    @Autowired
    private UserMngService userMngService;

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
            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
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
    
    @RequestMapping(value = "/InsertAgency", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO InsertAgency(@ModelAttribute("AgencyVO") @Valid AgencyVO agencyVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        UserVO userVO= new UserVO();
        try {
            
            String agency_cd = agencyService.getNewAgencyNo();
			agencyVO.setAgency_cd(agency_cd);            

            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    agencyVO.setEnt_user_id(member.getUserId());
            
            userVO.setCorp_cd(agency_cd);
            userVO.setCorp_type("AG");
            userVO.setUser_id(agencyVO.getCeo_id());
            userVO.setUser_nm(agencyVO.getCeo_nm());
            userVO.setUser_pwd(agencyVO.getCeo_pwd());
            userVO.setUser_gb("10");
            userVO.setAuth_grp_cd("AG1001");        // 대리점 대표 권한
            userVO.setHp_no(agencyVO.getCeo_tel_no());
            userVO.setTel_no(agencyVO.getCeo_tel_no());
            userVO.setEmail(agencyVO.getEmail());
            userVO.setZip_no(agencyVO.getCeo_zip_no());
            userVO.setAddr(agencyVO.getCeo_addr());
            userVO.setAddr_dtl(agencyVO.getCeo_addr_dtl());
            userVO.setUse_yn(agencyVO.getUse_yn());
            userVO.setEnt_user_id(member.getUserId());

            if (agencyService.InsertAgency(agencyVO, userVO)) {
                System.out.println("InsertAgency success");
                result.setResultCode("S000");
                result.setResultMsg("Agency creation successful.");                
            } else {
                System.out.println("InsertAgency fail");
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

    @RequestMapping(value = "/UpdateAgency", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO UpdateAgency(@ModelAttribute("AgencyVO") @Valid AgencyVO agencyVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        UserVO userVO= new UserVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    agencyVO.setUpt_user_id(member.getUserId());
            // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ceo pwd : " + agencyVO.getCeo_pwd() );
            userVO.setUser_id(agencyVO.getCeo_id());
            userVO.setUser_nm(agencyVO.getCeo_nm());
            userVO.setUser_pwd(agencyVO.getCeo_pwd());
            userVO.setHp_no(agencyVO.getCeo_tel_no());
            userVO.setTel_no(agencyVO.getCeo_tel_no());
            userVO.setEmail(agencyVO.getEmail());
            userVO.setZip_no(agencyVO.getCeo_zip_no());
            userVO.setAddr(agencyVO.getCeo_addr());
            userVO.setAddr_dtl(agencyVO.getCeo_addr_dtl());
            userVO.setUse_yn(agencyVO.getUse_yn());
            userVO.setUpt_user_id(member.getUserId());

            if (agencyService.UpdateAgency(agencyVO, userVO)) {
                System.out.println("UpdateAgency  success");
                result.setResultCode("S000");
                result.setResultMsg("Agency Update successful.");     
                
            } else {
                System.out.println("UpdateAgency  Fail");
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


    @RequestMapping(value = "/UpdateAgencyCont", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO UpdateAgencyCont(@ModelAttribute("AgencyVO") @Valid AgencyVO agencyVO, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    agencyVO.setUpt_user_id(member.getUserId()); 

            if (agencyService.UpdateAgencyCont(agencyVO)) {
                System.out.println("UpdateAgency  success");
                result.setResultCode("S000");
                result.setResultMsg("Agency Update successful.");     
                
            } else {
                System.out.println("UpdateAgency  Fail");
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
 
