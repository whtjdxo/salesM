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

import com.web.manage.base.service.ChainService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.user.domain.UserVO;
import com.google.gson.Gson;
import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.base.domain.ChainVO;
import com.web.manage.base.domain.ChainVanVO;

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
                result.setResultMsg("Chain creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain creation Failed");
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
                result.setResultMsg("Chain update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain update Failed");
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
                result.setResultMsg("Credit update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Contract update Failed");
            e.printStackTrace();
        }
        return result;
    } 


    /* -----------------------------------------------------------------------------------------------------------------------------------------
    * chain_van 관리
    -----------------------------------------------------------------------------------------------------------------------------------------  */
    @RequestMapping("vanList")
    public @ResponseBody String vanList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
            
            System.out.println("van_chain_no >> " + hashmapParam.get("van_chain_no"));

            hashmapParam.put("chain_no", hashmapParam.get("van_chain_no"));
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = chainService.getChainVanList(hashmapParam);
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
 
    @RequestMapping(value = "/getVanIdDupChk", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO getVanIdDupChk(@RequestBody HashMap<String, Object> params) {        
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (chainService.getVanIdDupChk(params) == 0) {
                result.setResultCode("S000");
                result.setResultMsg("Available VAN ID.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("Duplicate VAN ID.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("VAN ID Check Error.");
            e.printStackTrace();
        }        
        return result;
    }
    

    @RequestMapping(value = "/insertChainVan", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertChainVan(@ModelAttribute("ChainVanVO") @Valid ChainVanVO chainVanVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            chainVanVo.setChain_no(chainVanVo.getVan_chain_no());
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainVanVo.setEnt_user_id(member.getUserId());

            if (chainService.insertChainVan(chainVanVo)) {
                System.out.println("Chain Van Create success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Van creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateChainVan", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChainVan(@ModelAttribute("ChainVanVO") @Valid ChainVanVO chainVanVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            chainVanVo.setChain_no(chainVanVo.getVan_chain_no());
    	    chainVanVo.setUpt_user_id(member.getUserId()); 

            if (chainService.updateChainVan(chainVanVo)) {
                System.out.println("Chain VanInfo Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Van Update successful.");
            } else {
                System.out.println("Chain VanInfo Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Van Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Van Update Failed");
            e.printStackTrace();
        }
        return result;
    }

    /* -----------------------------------------------------------------------------------------------------------------------------------------
    * chain_card 관리
    -----------------------------------------------------------------------------------------------------------------------------------------  */     
    @RequestMapping("cardList")
    public @ResponseBody String cardList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
            
            // System.out.println("card_chain_no >> " + hashmapParam.get("card_chain_no"));
            hashmapParam.put("chain_no", hashmapParam.get("card_chain_no"));
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = chainService.getChainCardList(hashmapParam);
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
 
    @RequestMapping(value = "/getCardDupChk", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO getCardDupChk(@RequestBody HashMap<String, Object> params) {        
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (chainService.getCardDupChk(params) == 0) {
                result.setResultCode("S000");
                result.setResultMsg("Available VAN ID.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("Duplicate VAN ID.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("VAN ID Check Error.");
            e.printStackTrace();
        }        
        return result;
    }
    

    @RequestMapping(value = "/insertChainCard", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertChainCard(@ModelAttribute("ChainCardVO") @Valid ChainCardVO chainCardVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            chainCardVo.setChain_no(chainCardVo.getCard_chain_no());
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainCardVo.setEnt_user_id(member.getUserId());

            if (chainService.insertChainCard(chainCardVo)) {
                System.out.println("Chain Card Create success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Card creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Card creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Card creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateChainCard", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChainCard(@ModelAttribute("ChainCardVO") @Valid ChainCardVO chainCardVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            chainCardVo.setChain_no(chainCardVo.getCard_chain_no());
    	    chainCardVo.setUpt_user_id(member.getUserId()); 

            if (chainService.updateChainCard(chainCardVo)) {
                System.out.println("Chain CardInfo Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Van Update successful.");
            } else {
                System.out.println("Chain CardInfo Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Card Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Card Update Failed");
            e.printStackTrace();
        }
        return result;
    }
    
}
