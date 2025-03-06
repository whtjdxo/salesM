package com.web.manage.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.util.ValidateUtil;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.system.domain.MenuVO;
import com.web.manage.system.service.MenuService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/manage/system/menu")
public class MenuController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6355703923059082755L;
	
	@Autowired
	private MenuService mapper;

	
	@RequestMapping(value="/view")
	public String view() {
		return "pages/system/menu";
	}
	
	@RequestMapping("/getMenuTreeRetrieve")
	public @ResponseBody HashMap<String, Object> getMenuTreeRetrieve(){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> hashmapTree = new HashMap<String, Object>();
		
		resultList = mapper.getMenuTreeRetrieve();
		
		int lft = 1;
		for (HashMap<String, Object> hashMap : resultList){
			String menuTp = (String) hashMap.get("menu_tp");
			String menuCd = (String) hashMap.get("menu_cd");
			hashMap.put("lft", lft);
			if (!"ROOT".equals((String) hashMap.get("menu_prnt_cd"))){
				String level = ((String) hashMap.get("menu_cd")).replace("M", "");
				if(level.length() == 2){
					hashMap.put("level", "1");
				} else if(level.length() == 4){
					hashMap.put("level", "2");
				} else if(level.length() == 6){
					hashMap.put("level", "3");
				} else if(level.length() == 8){
					hashMap.put("level", "4");
				}
			} else {
				hashMap.put("level", "0");
				hashMap.put("parentLv", "");
				hashMap.put("expanded", "true");
			}
			
			switch (menuTp) {
				case "A": hashMap.put("iconCls", "ui-icon-play"); break;
				case "P": hashMap.put("iconCls", "ui-icon-document"); break;
				case "D": hashMap.put("iconCls", "ui-icon-folder-open"); break;
				default: break;
			}
			
			if(menuTp.equals("A")){
				hashMap.put("isLeaf", "true");
				hashMap.put("rgt", lft+1);
				lft ++;
				
			} else if(menuTp.equals("P")){
				
				int subCnt = 0;
				for (HashMap<String, Object> subMap : resultList){
					if(menuCd.equals(subMap.get("menu_prnt_cd"))){
						subCnt ++;  
					}
				}
				hashMap.put("rgt", lft+(subCnt*2)+1);
				if(subCnt == 0) {
					hashMap.put("isLeaf", "true");
					lft ++;
				} else {
					hashMap.put("isLeaf", "false");
				}
				
			} else if(menuTp.equals("D")){
				int subCnt = 0;
				for (HashMap<String, Object> subMap : resultList){
					String subMenuCd = (String) subMap.get("menu_cd");
					if(subMenuCd.matches(menuCd+".*")){
						subCnt ++;  
					}
				}
				hashMap.put("rgt", lft+(subCnt*2)+1);
				if(subCnt == 0) {
					hashMap.put("isLeaf", "true");
					lft ++;
				} else {
					hashMap.put("isLeaf", "false");
					
				}
				if(subCnt == 1) hashMap.put("isLeaf", "true");;
			} else if (menuTp.equals("R")){
				hashMap.put("isLeaf", "false");
				hashMap.put("rgt", 1+(resultList.size()*2)+1);
			}  
			
			lft ++;
		}
		hashmapTree.put("total", resultList.size());
		hashmapTree.put("rows", resultList);
		
		return hashmapTree;
	}
	/**
	 * 코드목록 가져오기
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/upMenuRetrieve")
	public @ResponseBody List<HashMap<String, Object>> codeList(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			list = (List<HashMap<String, Object>>) mapper.codelist(hashmapParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 코드목록 생성
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/menuCreate", method= RequestMethod.POST)
		public@ResponseBody ReturnDataVO MenuCreate(@ModelAttribute("MenuVO") @Valid MenuVO menuVO, BindingResult bindResult, HttpSession session){
		ReturnDataVO result = new ReturnDataVO();
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		menuVO.setUser_id(member.getUserId());
		menuVO.setUpt_user_id(member.getUserId());
		try {
			result = ValidateUtil.validCheck(bindResult, result);
			
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.menuCreate(menuVO) == 1){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg("이미 존재하는 코드입니다.");
		}
    	return result;
    }


	/**
	 * 메뉴 수정 저장
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/menuUpdate", method= RequestMethod.POST)
	public@ResponseBody ReturnDataVO MenuUpdate(@ModelAttribute("MenuVO") @Valid MenuVO menuVO, BindingResult bindResult, HttpSession session){
		ReturnDataVO result = new ReturnDataVO();
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		menuVO.setUser_id(member.getUserId());
		menuVO.setUpt_user_id(member.getUserId());
		try {
			result = ValidateUtil.validCheck(bindResult, result);
			
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			if(mapper.menuUpdate(menuVO) == 1){
				result.setResultCode("S000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg("이미 존재하는 코드입니다.");
		}
		return result;
    }
	/**
	 * 메뉴목록 삭제
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/menuDelete", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO menuDelete(@ModelAttribute("menuVO") @Valid MenuVO menuVO, BindingResult bindResult, HttpSession session){
		ReturnDataVO result = new ReturnDataVO();
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		menuVO.setUser_id(member.getUserId());
		try {
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			int cnt = (Integer)mapper.getMenuCount(menuVO);
			if(cnt != 1){
				result.setResultCode("S999");
				result.setResultMsg("하위코드가 있는 코드는 삭제 할 수 없습니다.");
				return result;
			}
			if(mapper.menuDelete(menuVO) == 1){
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
