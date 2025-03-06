package com.web.manage.system.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.domain.PermissionVO;
import com.web.manage.system.mapper.PermissionMapper;


@Service
public class PermissionService {
	
	@Autowired
	private PermissionMapper mapper;

	public ArrayList<HashMap<String, Object>> siteListMenu1(HashMap<String, Object> hashmapParam) {
		// TODO Auto-generated method stub
		return mapper.siteListMenu1(hashmapParam);
	}

	public void permissionDelete(PermissionVO permissionVO) {
		// TODO Auto-generated method stub
		mapper.permissionDelete(permissionVO);
		
	}

	public int permissionInsert(PermissionVO input_permissionVO) {
		// TODO Auto-generated method stub
		return mapper.permissionInsert(input_permissionVO);
	}

}
