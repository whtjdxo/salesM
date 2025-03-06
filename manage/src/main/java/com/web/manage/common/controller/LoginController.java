package com.web.manage.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.LoginService;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	
	@RequestMapping("/login")
	public String login() {
		return "pages/login";
	}
	@RequestMapping("/chkLogin")
	public @ResponseBody ReturnDataVO chkLogin(@RequestBody HashMap<String, Object> loginMap, HttpServletRequest request, HttpServletResponse response) {
		ReturnDataVO result = new ReturnDataVO();
		HttpSession session = request.getSession(true);
		SessionVO loginUserVo = new SessionVO();
		loginUserVo = loginService.getUserRetrieve(loginMap);
		String authGrp = "";
		if(loginUserVo != null ){

			authGrp = loginService.getAuthGrp(loginUserVo.getUserId());
			loginUserVo.setAuthGrpCd(authGrp);

			List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String,Object>>();
			List<HashMap<String, Object>> menu2ndList = new ArrayList<HashMap<String,Object>>();
			List<HashMap<String, Object>> menu3rdList = new ArrayList<HashMap<String,Object>>();
			menuList = loginService.getMenuRetrieve(loginUserVo);
			loginUserVo.setMenu(menuList);
			menu2ndList = loginService.getMenuRetrieve2nd(loginUserVo);
			loginUserVo.setMenu2nd(menu2ndList);
			menu3rdList = loginService.getMenuRetrieve3rd(loginUserVo);
			loginUserVo.setMenu3rd(menu3rdList);
			

			session.setAttribute("userId", loginUserVo.getUserId());
			session.setAttribute("S_USER", loginUserVo);
			session.setAttribute("S_LOGIN_YN"	, "Y");
			session.setAttribute("theme", "default");
			result.setResultCode("00");
			result.setResultMsg("로그인 성공");
		} else {
			result.setResultCode("99");
			result.setResultMsg("로그인 실패");
		}

		return result;
	}
	
}
