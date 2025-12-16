package com.web.manage.base.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.web.manage.base.service.ChainService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.web.manage.system.domain.BoardVO;
import com.web.manage.user.domain.UserVO;
import com.google.gson.Gson;
import com.web.common.util.DateUtil;
import com.web.manage.base.domain.ChainCardVO;
import com.web.manage.base.domain.ChainCounselVO;
import com.web.manage.base.domain.ChainFileVO;
import com.web.manage.base.domain.ChainVO;
import com.web.manage.base.domain.ChainVanVO;
import com.web.manage.base.domain.LinkChainVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/base/organ/chainMng")
public class ChainController {
 
    @Autowired
    private ChainService chainService; 
    @Autowired
    private CommonService commonService;
    

    @RequestMapping("view")
    public String view() {
        return "pages/base/chainMng";
    }

    @Value("${global.fileStorePath}")
	String origin_fileStorePath;

    @RequestMapping("list")
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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

            // hashmapParam.put("srch_chain_name", StringUtil.nullCheck((String) pageing.getSearch().get("srch_chain_name"), ""));
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
            // String chain_no = chainService.getNewChainNo();
            String chain_no = commonService.getJobSeq("TB_CHAIN", "CHAIN_NO");
			chainVo.setChain_no(chain_no);

            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainVo.setEnt_user_id(member.getUserId());

            userVO.setCorp_cd(chain_no);
            userVO.setCorp_type("CH");
            userVO.setUser_id(chainVo.getCeo_id());
            userVO.setUser_nm(chainVo.getCeo_nm());
            userVO.setUser_pwd(chainVo.getCeo_pwd());
            userVO.setUser_gb("20");
            userVO.setAuth_grp_cd("AG2001");        // 가맹점 대표 권한
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

            // System.out.println(chainVo);   

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
        // System.out.println("call getCardDupChk");
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
                result.setResultMsg("Chain Card Update successful.");
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


    /* -----------------------------------------------------------------------------------------------------------------------------------------
    * Link Chain 관리
    -----------------------------------------------------------------------------------------------------------------------------------------  */     
    @RequestMapping("linkChainList")
    public @ResponseBody String getLinkChainList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
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

            list = chainService.getLinkChainList(hashmapParam);
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

    @RequestMapping(value = "/insertLinkChain", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertLinkChain(@ModelAttribute("LinkChainVO") @Valid LinkChainVO linkChainVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    linkChainVo.setEnt_user_id(member.getUserId());

            if (chainService.insertLinkChain(linkChainVo)) {
                System.out.println("Chain Link Create success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Link creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Link creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Link creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateLinkChain", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateLinkChain(@ModelAttribute("LinkChainVO") @Valid LinkChainVO linkChainVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER"); 
    	    linkChainVo.setUpt_user_id(member.getUserId()); 

            if (chainService.updateLinkChain(linkChainVo)) {
                System.out.println("Chain Link Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Link Update successful.");
            } else {
                System.out.println("Chain Link Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Link Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Link Update Failed");
            e.printStackTrace();
        }
        return result;
    }


    /* -----------------------------------------------------------------------------------------------------------------------------------------
    * chain_File 관리
    -----------------------------------------------------------------------------------------------------------------------------------------  */     
    @RequestMapping("fileList")
    public @ResponseBody String fileList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);
             
            hashmapParam.put("chain_no", hashmapParam.get("card_chain_no"));
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

            list = chainService.getChainFileList(hashmapParam);
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
 
    @RequestMapping(value = "/getFileDupChk", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO getFileDupChk(@RequestBody HashMap<String, Object> params) {        
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (chainService.getFileDupChk(params) == 0) {
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

    @RequestMapping(value = "/insertChainFile", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertChainFile(@ModelAttribute("ChainFileVO") @Valid ChainFileVO chainFileVo, BindingResult bindingResult, MultipartHttpServletRequest multiRequest, HttpSession session) {    
        
        System.out.println("파일 업로드 요청 도착");

        ReturnDataVO result = new ReturnDataVO(); 
        try {
            chainFileVo.setChain_no(chainFileVo.getFile_chain_no());
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    chainFileVo.setEnt_user_id(member.getUserId());
            if(chainFileVo.getFile_upload() != null) {
				if(chainFileVo.getFile_upload().size() > 0) {					
					for(MultipartFile file : chainFileVo.getFile_upload()) {						
					  
	                    // String pathString = origin_fileStorePath + chainFileVo.getFile_chain_no() ;
                        String pathString = Paths.get(origin_fileStorePath, chainFileVo.getFile_chain_no()).toString();
	                    String fileSeq = commonService.getJobSeq("TB_CHAIN_FILE", "FILE_SEQ");                        

                        System.out.println("pathString : " + pathString);    
	                   
                        File saveFolder = new File(pathString);
	                    if(!saveFolder.exists() || saveFolder.isFile()) {
	                        saveFolder.mkdirs();
	                    }
	                    
	                    String originName = file.getOriginalFilename();
	                    String fileSize = String.valueOf(file.getSize());

                        int pos = originName.lastIndexOf(".");
	                    String fileExt = originName.substring(pos + 1);
	                    String newName = chainFileVo.getFile_gb() + "_"+ fileSeq + Math.round(Math.random() * 100);
	                    
	                    // filePath = File.separator + newName + "." + fileExt;
                        String savePath = pathString + File.separator + newName + "." + fileExt;

                        System.out.println("newName : " + newName);
                        System.out.println("filePath : " + savePath);

	                    file.transferTo(new File(savePath));

	                    // String savePath = "/upload/" + chainFileVo.getFile_chain_no()  + "/" + newName + "." + fileExt;	 
                        System.out.println("savePath : " + savePath); 

	                    // File fileExist = new File(pathString + "/" + newName + "." + fileExt);						
                        File fileExist = new File(savePath);
	                    
	                    // 파일 업로드 확인
	                    if(fileExist.exists()) {	         
                            chainFileVo.setFile_seq(fileSeq);              	
	        			    chainFileVo.setOrigin_file_nm(originName);
	        			    chainFileVo.setFile_nm(newName);
	        			    chainFileVo.setFile_path(savePath);	      
                            chainFileVo.setFile_size(fileSize);  
                            chainFileVo.setFile_ext(fileExt);	

	        			    if (chainService.insertChainFile(chainFileVo)) {
                                System.out.println("Chain File Create success");
                                result.setResultCode("S000");
                                result.setResultMsg("Chain File creation successful.");
                            } else {
                                System.out.println("chain File Create fail");
                                result.setResultCode("F000");
                                result.setResultMsg("Chain File Creation Failed");
                            }
	                    } else {
                            System.out.println("File not Exists [fail] ");
                            result.setResultCode("F000");
                            result.setResultMsg("Chain File Creation Failed");
                        }
					}					
				}
			}            
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain File creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateChainFile", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChainFile(@ModelAttribute("ChainFileVO") @Valid ChainFileVO chainFileVo, BindingResult bindingResult, MultipartHttpServletRequest multiRequest, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            chainFileVo.setChain_no(chainFileVo.getFile_chain_no());
    	    chainFileVo.setUpt_user_id(member.getUserId()); 

            if(chainFileVo.getFile_upload() != null) {
				if(chainFileVo.getFile_upload().size() > 0) {					
					for(MultipartFile file : chainFileVo.getFile_upload()) {						
						String filePath = "";
                        
	                    String pathString = origin_fileStorePath + chainFileVo.getFile_chain_no() ;	                    

	                    File saveFolder = new File(pathString);
	                    if(!saveFolder.exists() || saveFolder.isFile()) {
	                        saveFolder.mkdirs();
	                    }
	                    
	                    String originName = file.getOriginalFilename();
	                    String fileSize = String.valueOf(file.getSize());

                        int pos = originName.lastIndexOf(".");
	                    String fileExt = originName.substring(pos + 1);
	                    String newName = chainFileVo.getFile_gb() + chainFileVo.getFile_seq() + Math.round(Math.random() * 100);
	                    
	                    filePath = File.separator + newName + "." + fileExt;
                        System.out.println("newName : " + newName);
                        System.out.println("filePath : " + filePath);

	                    file.transferTo(new File(filePath));

	                    String savePath = "/upload/" + chainFileVo.getFile_chain_no()  + "/" + newName + "." + fileExt;	 
                        System.out.println("savePath : " + savePath); 

	                    File fileExist = new File(pathString + "/" + newName + "." + fileExt);						
	                    
	                    // 파일 업로드 확인
	                    if(fileExist.exists()) {
	        			    chainFileVo.setOrigin_file_nm(originName);
	        			    chainFileVo.setFile_nm(newName);
	        			    chainFileVo.setFile_path(savePath);	      
                            chainFileVo.setFile_size(fileSize);
	        			    if (chainService.insertChainFile(chainFileVo)) {
                                System.out.println("Chain File Create success");
                                result.setResultCode("S000");
                                result.setResultMsg("Chain File creation successful.");
                            } else {
                                System.out.println("chainCreate fail");
                                result.setResultCode("F000");
                                result.setResultMsg("Chain File Creation Failed");
                            }
	                    } 
					}
					
				}
			}

            if (chainService.updateChainFile(chainFileVo)) {
                System.out.println("Chain File Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain File Update successful.");
            } else {
                System.out.println("Chain File Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain File Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain File Update Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/deleteChainFile", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO deleteChainFile(@ModelAttribute("ChainFileVO") @Valid ChainFileVO chainFileVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();         
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            chainFileVo.setChain_no(chainFileVo.getFile_chain_no());
    	    chainFileVo.setUpt_user_id(member.getUserId());  
            // System.out.println(chainFileVo.getFile_seq());
            if (chainService.deleteChainFile(chainFileVo)) {
                System.out.println("Chain File Delete  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain File Delete successful.");
            } else {
                System.out.println("Chain File Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain File Delete Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain File Delete Failed");
            e.printStackTrace();
        }
        return result;
    }


    /* -----------------------------------------------------------------------------------------------------------------------------------------
    * Chain Counsel 관리
    -----------------------------------------------------------------------------------------------------------------------------------------  */     
    @RequestMapping("counselList")
    public @ResponseBody String getChainCounselList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();        
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

            list = chainService.getChainCounselList(hashmapParam);
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

    @RequestMapping(value = "/insertCounsel", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertChainCounsel(@ModelAttribute("ChainCounselVO") @Valid ChainCounselVO counselVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    counselVo.setEnt_user_id(member.getUserId());

            if (chainService.insertChainCounsel(counselVo)) {
                System.out.println("Chain Counsel Create success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Counsel creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Counsel creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Counsel creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateCounsel", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateChainCounsel(@ModelAttribute("ChainCounselVO") @Valid ChainCounselVO counselVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER"); 
    	    counselVo.setUpt_user_id(member.getUserId()); 

            if (chainService.updateChainCounsel(counselVo)) {
                // System.out.println("Chain Counsel Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Counsel Update successful.");
            } else {
                System.out.println("Chain Link Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Counsel Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Counsel Update Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/deleteCounsel", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO deleteChainCounsel(@ModelAttribute("ChainCounselVO") @Valid ChainCounselVO counselVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();         
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER"); 
    	    counselVo.setUpt_user_id(member.getUserId());  
            // System.out.println(chainFileVo.getFile_seq());
            if (chainService.deleteChainCounsel(counselVo)) {
                System.out.println("Chain Counsel Delete  success");
                result.setResultCode("S000");
                result.setResultMsg("Chain Counsel Delete successful.");
            } else {
                System.out.println("Chain Counsel Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Chain Counsel Delete Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Chain Counsel Delete Failed");
            e.printStackTrace();
        }
        return result;
    }
    
}
