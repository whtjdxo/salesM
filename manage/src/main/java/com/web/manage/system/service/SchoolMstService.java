package com.web.manage.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.domain.SchoolMstVO;
import com.web.manage.system.mapper.SchoolMstMapper;

import jakarta.validation.Valid;


@Service
public class SchoolMstService {
	
	@Autowired
	private SchoolMstMapper mapper;
	
	public int getQueryTotalCnt(HashMap<String, Object> hashmapParam) {
		return mapper.getQueryTotalCnt(hashmapParam);
	}
	
	public String getSchCdRetrieve() {
		return mapper.getSchCdRetrieve();
	}
	
	public List<HashMap<String, Object>> getSchListRetrieve(HashMap<String, Object> hashmapParam) {
		return mapper.getSchListRetrieve(hashmapParam);
	}
	
	public List<HashMap<String, Object>> getSchoolMstListRetrieve(HashMap<String, Object> hashmapParam) {
		return mapper.getSchoolMstListRetrieve(hashmapParam);
	}
	
	public int schInfoCreate(@Valid SchoolMstVO schoolMstVO) {
		return mapper.schInfoCreate(schoolMstVO);
	}

	public int schInfoUpdate(@Valid SchoolMstVO schoolMstVO) {
		return mapper.schInfoUpdate(schoolMstVO);
	}
	
}