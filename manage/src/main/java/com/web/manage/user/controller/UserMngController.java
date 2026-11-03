package com.web.manage.user.controller;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.web.common.util.StringUtil;
import com.web.manage.user.domain.UserVO;
import com.web.manage.user.service.UserMngService;
import com.web.manage.base.domain.AgencyVO;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/base/user/userMng")
public class UserMngController {

    private static final String TOTP_RESET_CSRF_TOKEN = "S_TOTP_RESET_CSRF_TOKEN";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UserMngService userMngService;

    @RequestMapping("view")
    public String view(HttpSession session, Model model) {
        String csrfToken = (String) session.getAttribute(TOTP_RESET_CSRF_TOKEN);
        if (csrfToken == null) {
            byte[] tokenBytes = new byte[32];
            RANDOM.nextBytes(tokenBytes);
            csrfToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
            session.setAttribute(TOTP_RESET_CSRF_TOKEN, csrfToken);
        }
        model.addAttribute("totpResetToken", csrfToken);
        return "pages/user/userMng";
    }

    @RequestMapping("/list")
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Gson gson = new Gson(); 
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpType", member.getUserCorpType());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
        hashmapParam.put("userAuthGrpCd", member.getAuthGrpCd());
		String jString = null;
        System.out.println("..............");
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

            // System.out.println(hashmapParam);

            list = userMngService.userMngListRetrieve(hashmapParam);
            int records =  userMngService.getQueryTotalCnt();

			 pageing.setRecords(records);
			 pageing.setTotal((int) Math.ceil((double)records / (double)pageing.getLength()));

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

    @RequestMapping(value="/getCorpList", method=RequestMethod.POST)
    public @ResponseBody List<HashMap<String, Object>> getCorpList(@RequestParam HashMap<String, Object> hashmapParam) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			list = (List<HashMap<String, Object>>) userMngService.getCorpList(hashmapParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }

    @RequestMapping(value = "/getUserIdDupChk", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO UserIdDupChk(@RequestParam("user_id") String user_id) {
        // System.out.println("Checking duplicate USER ID: " + user_id); 
        ReturnDataVO result = new ReturnDataVO();
        try {
            if (userMngService.getUserIdDupChk(user_id) == 0) {
                result.setResultCode("S000");
                result.setResultMsg("사용가능 한 ID 입니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("사용중인 ID 입니다.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("USER creation failed.");
            e.printStackTrace();
        }        
        return result;
    }
    
	@RequestMapping("/create")
	public @ResponseBody ReturnDataVO create(@ModelAttribute("UserVO") @Valid UserVO userVO, BindingResult bindingResult, HttpSession session) {
		ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            userVO.setEnt_user_id(member.getUserId());
            
            if(userMngService.userCreate(userVO)){
				result.setResultCode("S000");
				result.setResultMsg("사용자등록이 완료되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("사용자등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("등록에 실패하였습니다.");
            e.printStackTrace();
        }
        return result;
	}
	@RequestMapping("/update")
	public @ResponseBody ReturnDataVO update(@ModelAttribute("UserVO") @Valid UserVO userVO, BindingResult bindingResult, HttpSession session) {
		ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            userVO.setUpt_user_id(member.getUserId());
            
            if(userMngService.userUpdate(userVO)){
				result.setResultCode("S000");
				result.setResultMsg("사용자등록이 완료되었습니다.");
            } else {
                result.setResultCode("F000");
                result.setResultMsg("사용자등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("등록에 실패하였습니다.");
            e.printStackTrace();
        }
        return result;
	}

    // 2중인증 적용시해제
    // @RequestMapping(value = "/totpReset", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO resetTotp(@RequestParam("user_id") String userId,
            @RequestParam("csrf_token") String csrfToken, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        if (member == null || !"AG0001".equals(member.getAuthGrpCd())) {
            result.setResultCode("F000");
            result.setResultMsg("시스템 관리자만 2단계 인증을 초기화할 수 있습니다.");
            return result;
        }
        String expectedToken = (String) session.getAttribute(TOTP_RESET_CSRF_TOKEN);
        if (expectedToken == null || !MessageDigest.isEqual(
                expectedToken.getBytes(StandardCharsets.UTF_8), csrfToken.getBytes(StandardCharsets.UTF_8))) {
            result.setResultCode("F000");
            result.setResultMsg("요청을 확인할 수 없습니다. 화면을 새로고침한 뒤 다시 시도해 주세요.");
            return result;
        }
        if (member.getUserId().equals(userId)) {
            result.setResultCode("F000");
            result.setResultMsg("현재 로그인한 계정은 다른 시스템 관리자가 초기화해야 합니다.");
            return result;
        }

        if (userMngService.resetTotp(userId) == 1) {
            result.setResultCode("S000");
            result.setResultMsg("2단계 인증이 초기화되었습니다. 사용자는 다음 로그인 때 다시 등록해야 합니다.");
        } else {
            result.setResultCode("F000");
            result.setResultMsg("사용자를 찾을 수 없거나 초기화에 실패했습니다.");
        }
        return result;
    }
    
}
