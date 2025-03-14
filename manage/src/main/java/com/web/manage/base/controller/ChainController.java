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

import com.web.manage.base.service.ChainService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.user.domain.UserVO;
import com.google.gson.Gson;
import com.web.manage.base.domain.ChainVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/base/organ/chainMng")
public class ChainController {

    @Autowired
    private ChainService chainService;

    @RequestMapping("view")
    public String view() {
        return "pages/base/chainMng";
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

            // hashmapParam.put("srch_chain_name", StringUtil.nullCheck((String) pageing.getSearch().get("srch_chain_name"), ""));
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = chainService.getChainList(hashmapParam);
            int records = chainService.getQueryTotalCnt();

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
    public @ResponseBody int ceoIdDupChk(@RequestBody String ceo_id) {
        return chainService.getCeoIdDupChk(ceo_id);
    }
    
    @RequestMapping(value = "/insertChain", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertChain(@ModelAttribute("ChainVO") @Valid ChainVO chainVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        UserVO userVO = new UserVO();
        try {
            String chain_no = chainService.getNewChainNo();
			chainVo.setChain_no(chain_no);

            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainVo.setEnt_user_id(member.getUserId());

            userVO.setCorp_cd(chain_no);
            userVO.setCorp_type("CH");
            userVO.setUser_id(chainVo.getCeo_id());
            userVO.setUser_nm(chainVo.getCeo_nm());
            userVO.setUser_pwd(chainVo.getCeo_pwd());
            userVO.setUser_gb("20");
            userVO.setAuth_grp_cd("AG2001");        // 대리점 대표 권한
            userVO.setHp_no(chainVo.getCeo_tel_no());
            userVO.setTel_no(chainVo.getCeo_tel_no());
            userVO.setEmail(chainVo.getEmail());
            userVO.setZip_no(chainVo.getCeo_zip_no());
            userVO.setAddr(chainVo.getCeo_addr());
            userVO.setAddr_dtl(chainVo.getCeo_addr_dtl());
            userVO.setUse_yn(chainVo.getUse_yn());
            userVO.setEnt_user_id(member.getUserId());

            if (chainService.insertChain(chainVo, userVO)) {
                System.out.println("chainCreate success");
                result.setResultCode("S000");
                result.setResultMsg("Chain creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain creation failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain creation failed.");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateChain", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChain(@ModelAttribute("ChainVO") @Valid ChainVO chainVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        UserVO  userVO = new UserVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainVo.setUpt_user_id(member.getUserId());
 
            userVO.setUser_id(chainVo.getCeo_id());
            userVO.setUser_nm(chainVo.getCeo_nm());
            userVO.setUser_pwd(chainVo.getCeo_pwd());
            userVO.setHp_no(chainVo.getCeo_tel_no());
            userVO.setTel_no(chainVo.getCeo_tel_no());
            userVO.setEmail(chainVo.getEmail());
            userVO.setZip_no(chainVo.getCeo_zip_no());
            userVO.setAddr(chainVo.getCeo_addr());
            userVO.setAddr_dtl(chainVo.getCeo_addr_dtl());
            userVO.setUse_yn(chainVo.getUse_yn());
            userVO.setUpt_user_id(member.getUserId());

            if (chainService.updateChain(chainVo, userVO)) {
                System.out.println("chainUpdate  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain update successful.");
            } else {
                System.out.println("chainUpdate  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain update failed.");
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping(value = "/updateChainCont", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChainCont(@ModelAttribute("ChainVO") @Valid ChainVO chainVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();        
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            
    	    chainVo.setUpt_user_id(member.getUserId()); 
            chainVo.setTot_limit_amt(chainVo.getTot_limit_amt() == null ? "0" : chainVo.getTot_limit_amt().replaceAll(",", ""));               
            chainVo.setUnit_limit_amt(chainVo.getUnit_limit_amt() == null ? "0" : chainVo.getUnit_limit_amt().replaceAll(",", ""));
            chainVo.setDay_use_rate(chainVo.getDay_use_rate() == null ? "0" : chainVo.getDay_use_rate().replaceAll(",", ""));
            chainVo.setDay_use_amt(chainVo.getDay_use_amt() == null ? "0" : chainVo.getDay_use_amt().replaceAll(",", ""));
            chainVo.setTot_use_amt(chainVo.getTot_use_amt() == null ? "0" : chainVo.getTot_use_amt().replaceAll(",", ""));
            chainVo.setRemit_trans_fee(chainVo.getRemit_trans_fee() == null ? "0" : chainVo.getRemit_trans_fee().replaceAll(",", ""));

            System.out.println(chainVo);   

            if (chainService.updateChainCont(chainVo)) {
                System.out.println("chain Contract Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain update successful.");
            } else {
                System.out.println("Chain Contract Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Credit update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Contract update failed.");
            e.printStackTrace();
        }
        return result;
    }
}
