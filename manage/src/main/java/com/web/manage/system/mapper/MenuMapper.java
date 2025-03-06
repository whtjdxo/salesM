package com.web.manage.system.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.system.domain.MenuVO;


@Mapper
public interface MenuMapper {

	List<HashMap<String, Object>> getMenuTreeRetrieve();

	List<HashMap<String, Object>> codelist(HashMap<String, String> hashmapParam);

	int menuCreate(MenuVO menuVO);

	int menuUpdate(MenuVO menuVO);

	Integer getMenuCount(MenuVO menuVO);

	int menuDelete(MenuVO menuVO);

}
