package com.web.manage.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.domain.MenuVO;
import com.web.manage.system.mapper.MenuMapper;


@Service
public class MenuService {

	@Autowired
	private MenuMapper mapper;
	
	public List<HashMap<String, Object>> getMenuTreeRetrieve() {
		// TODO Auto-generated method stub
		return mapper.getMenuTreeRetrieve();
	}

	public List<HashMap<String, Object>> codelist(HashMap<String, String> hashmapParam) {
		// TODO Auto-generated method stub
		return mapper.codelist(hashmapParam);
	}

	public int menuCreate(MenuVO menuVO) {
		// TODO Auto-generated method stub
		return mapper.menuCreate(menuVO);
	}

	public int menuUpdate(MenuVO menuVO) {
		// TODO Auto-generated method stub
		return mapper.menuUpdate(menuVO);
	}

	public Integer getMenuCount(MenuVO menuVO) {
		// TODO Auto-generated method stub
		return mapper.getMenuCount(menuVO);
	}

	public int menuDelete(MenuVO menuVO) {
		// TODO Auto-generated method stub
		return mapper.menuDelete(menuVO);
	}

}
