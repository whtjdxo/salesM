package com.web.manage.common.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.common.domain.SessionVO;

@Mapper
public interface LoginMapper {

	int chkLogin(HashMap<String, Object> loginMap);

	SessionVO getUserRetrieve(HashMap<String, Object> loginMap);

    String getAuthGrp(String userId);

	List<HashMap<String, Object>> getMenuRetrieve(SessionVO loginUserVo);

	List<HashMap<String, Object>> getMenuRetrieve2nd(SessionVO loginUserVo);

    List<HashMap<String, Object>> getMenuRetrieve3rd(SessionVO loginUserVo);

}
