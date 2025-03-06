package com.web.manage.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.system.domain.PermissionVO;
import com.web.manage.system.service.PermissionService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/manage/system/permission")
public class PermissionController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4599467413630655742L;
	
	@Autowired
	private PermissionService mapper;
	
	@RequestMapping(value="/view")
	public String view() {
		return "pages/system/auth";
	}
	
	@RequestMapping(value="/getMenuTreeRetrieve")
    public @ResponseBody List<HashMap<String, Object>> listRetrieve1(@RequestParam HashMap<String, Object> hashmapParam){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String,Object>>();
    	List<HashMap<String, Object>> resultData = new ArrayList<HashMap<String,Object>>();
    	
    	
    	String auth_grp_cd = (String) hashmapParam.get("auth_grp_cd");
		hashmapParam.put("auth_grp_cd", auth_grp_cd); 		
		try {
			resultList =  mapper.siteListMenu1(hashmapParam);
			for(HashMap<String, Object> map : resultList){
				if("selected".equals(map.get("state"))){
					HashMap<String, Object> state = new HashMap<String, Object>();
					state.put("selected", true);
					map.put("state", state);
				}
				resultData.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
    }
	@RequestMapping(value="/update", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO PermissionUpdate(@ModelAttribute("PermissionVO") @Valid PermissionVO permissionVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	permissionVO.setUser_id(member.getUserId());
		try {
			if (bindResult.hasErrors()) {
	            // 에러 출력
	            List<ObjectError> list = bindResult.getAllErrors();
	            for (ObjectError e : list) {
	            	result.setResultMsg(e.getDefaultMessage());
	            }
	            result.setResultCode("V999");
	            
	            return result;
	        }
			
			mapper.permissionDelete(permissionVO);

			String[] menu_cd_list = permissionVO.getMenu_cd().split(",");
			for (int i = 0;i < menu_cd_list.length;i++){
				String menu_cd = menu_cd_list[i];
				PermissionVO input_permissionVO = new PermissionVO();
				
				input_permissionVO.setAuth_grp_cd(permissionVO.getAuth_grp_cd());
				input_permissionVO.setMenu_cd(menu_cd);
				input_permissionVO.setSite_gb_cd(permissionVO.getSite_gb_cd());
				input_permissionVO.setUser_id(permissionVO.getUser_id());
				if(mapper.permissionInsert(input_permissionVO) == 1){
					result.setResultCode("S000");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg("이미 존재하는 코드입니다.");
		}
    	return result;
    }
}
