package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkDayMapper {

    List<HashMap<String, Object>> getWorkDayList(HashMap<String, Object> hashmapParam);

    boolean insertWorkDay(HashMap<String, Object> workDayData);
}
