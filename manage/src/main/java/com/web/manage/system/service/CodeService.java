package com.web.manage.system.service;

import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.domain.GroupCodeVO;
import com.web.manage.system.domain.LowCodeVO;
import com.web.manage.system.mapper.CodeMapper;

@Service
public class CodeService {
	
	@Autowired
	private CodeMapper mapper;

	public List<HashMap<String, Object>> getGroupCodeRetrieve(HashMap<String, Object> hashmapParam) {
		// TODO Auto-generated method stub
		return mapper.getGroupCodeRetrieve(hashmapParam);
	}

	public List<HashMap<String, Object>> getLowerCodeRetrieve(HashMap<String, Object> hashmapParam) {
		// TODO Auto-generated method stub
		return mapper.getLowerCodeRetrieve(hashmapParam);
	}

	public int getQueryTotalCnt() {
		// TODO Auto-generated method stub
		return mapper.getQueryTotalCnt();
	}
	public int groupCodeCreate(GroupCodeVO groupCodeVO) {
		// TODO Auto-generated method stub
		return mapper.groupCodeCreate(groupCodeVO);
	}

	public int groupCodeUpdate(GroupCodeVO groupCodeVO) {
		// TODO Auto-generated method stub
		return mapper.groupCodeUpdate(groupCodeVO);
	}

	public int getLowCodeCount(GroupCodeVO groupCodeVO) {
		// TODO Auto-generated method stub
		return mapper.getLowCodeCount(groupCodeVO);
	}

	public int groupCodeDelete(GroupCodeVO groupCodeVO) {
		// TODO Auto-generated method stub
		return mapper.groupCodeDelete(groupCodeVO);
	}

	public int lowCodeCreate(LowCodeVO lowCodeVO) {
		// TODO Auto-generated method stub
		return mapper.lowCodeCreate(lowCodeVO);
	}

	public int lowCodeUpdate(LowCodeVO lowCodeVO) {
		// TODO Auto-generated method stub
		return mapper.lowCodeUpdate(lowCodeVO);
	}

	public int lowCodeDelete(LowCodeVO lowCodeVO) {
		// TODO Auto-generated method stub
		return mapper.lowCodeDelete(lowCodeVO);
	}

}
