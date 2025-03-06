package com.web.manage.system.controller;

import java.io.Serializable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.util.ValidateUtil;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.system.domain.GroupCodeVO;
import com.web.manage.system.domain.LowCodeVO;
import com.web.manage.system.service.CodeService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/manage/system/code")
public class CodeController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -814484684324010707L;
	
	static final Logger logger = (Logger) LoggerFactory.getLogger(CodeController.class);
	
	@Autowired
	private CodeService mapper;
	
	@RequestMapping(value="/view")
	public String view() {
		return "pages/system/code";
	}
	
	/**
	 * 그룹코드 목록 조회
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/groupCodeRetrieve", method= RequestMethod.POST)
    public @ResponseBody HashMap<String, Object> groupCodeRetrieve(@RequestBody HashMap<String, Object> hashmapParam){
    	List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String,Object>>(); 
    	HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		try {
			PageingVO pageing = new PageingVO();
			pageing.setPageingVO(hashmapParam);
			int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
			hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
			hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));

			resultList =  mapper.getGroupCodeRetrieve(hashmapParam);
			hashmapResult.put("data", resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashmapResult;
    }
	/**
	 * 하위코드 목록 조회
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam, PageingVO
	 * @return json
	 */
	@RequestMapping(value="/lowerCodeRetrieve", method= RequestMethod.POST)
    public @ResponseBody HashMap<String, Object> lowerCodeRetrieve(@RequestBody HashMap<String, Object> hashmapParam){
    	List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String,Object>>(); 
    	HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		try {
			PageingVO pageing = new PageingVO();
			pageing.setPageingVO(hashmapParam);
			int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
			hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
			hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));

			resultList =  mapper.getLowerCodeRetrieve(hashmapParam);
			hashmapResult.put("data", resultList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashmapResult;
    }
	
	/**
	 * 그룹코드 목록 저장
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/groupCodeCreate", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO groupCodeCreate(@ModelAttribute("groupCodeVO") @Valid GroupCodeVO groupCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	groupCodeVO.setUser_id(member.getUserId());
		try {
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.groupCodeCreate(groupCodeVO) == 1){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }
	
    /**
	 * 그룹코드 목록 수정
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/groupCodeUpdate", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO groupCodeUpdate(@ModelAttribute("groupCodeVO") @Valid GroupCodeVO groupCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	groupCodeVO.setUser_id(member.getUserId());
		try {
			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			if(mapper.groupCodeUpdate(groupCodeVO) > 0){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }
	
    /**
	 * 그룹코드 목록 삭제
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/groupCodeDelete", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO groupCodeDelete(@ModelAttribute("groupCodeVO") @Valid GroupCodeVO groupCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	groupCodeVO.setUser_id(member.getUserId());
		try {
			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			int cnt = mapper.getLowCodeCount(groupCodeVO);
			if(cnt != 0){
				result.setResultCode("S999");
				result.setResultMsg("하위코드가 있는 코드는 삭제 할 수 없습니다.");
				return result;
			}
			
			if(mapper.groupCodeDelete(groupCodeVO) == 1){
				result.setResultCode("S000");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }
	
    /**
	 * 하위코드 목록 저장
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/lowCodeCreate", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO lowCodeCreate(@ModelAttribute("lowCodeVO") @Valid LowCodeVO lowCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	lowCodeVO.setUser_id(member.getUserId());
		try {
			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.lowCodeCreate(lowCodeVO) == 1){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }
	
    /**
	 * 하위코드 목록 수정
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/lowCodeUpdate", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO lowCodeUpdate(@ModelAttribute("lowCodeVO") @Valid LowCodeVO lowCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	lowCodeVO.setUser_id(member.getUserId());
		try {
			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.lowCodeUpdate(lowCodeVO) > 0){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }
	
    /**
	 * 하위코드 목록 삭제
	 * @author 김솔람
	 * @since 2019.03.20
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/lowCodeDelete", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO lowCodeDelete(@ModelAttribute("lowCodeVO") @Valid LowCodeVO lowCodeVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	lowCodeVO.setUser_id(member.getUserId());
		try {
			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.lowCodeDelete(lowCodeVO) == 1){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
		}
    	return result;
    }

}
