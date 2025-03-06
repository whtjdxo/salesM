package com.web.manage.common.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	int getQueryTotalCnt();

}
