package com.web.manage.common.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

    List<HashMap<String, Object>> getTotalCodelist(HashMap<String, String> hashmapParam);

}
