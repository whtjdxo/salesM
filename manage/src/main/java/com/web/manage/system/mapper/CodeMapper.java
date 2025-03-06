package com.web.manage.system.mapper;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.system.domain.GroupCodeVO;
import com.web.manage.system.domain.LowCodeVO;


@Mapper
public interface CodeMapper {

	List<HashMap<String, Object>> getGroupCodeRetrieve(HashMap<String, Object> hashmapParam);

	List<HashMap<String, Object>> getLowerCodeRetrieve(HashMap<String, Object> hashmapParam);

	int getQueryTotalCnt();

	List<HashMap<String, Object>> codelist(HashMap<String, String> hashmapParam);

	int groupCodeCreate(GroupCodeVO groupCodeVO);

	int groupCodeUpdate(GroupCodeVO groupCodeVO);

	int getLowCodeCount(GroupCodeVO groupCodeVO);

	int groupCodeDelete(GroupCodeVO groupCodeVO);

	int lowCodeCreate(LowCodeVO lowCodeVO);

	int lowCodeUpdate(LowCodeVO lowCodeVO);

	int lowCodeDelete(LowCodeVO lowCodeVO);
	
}
