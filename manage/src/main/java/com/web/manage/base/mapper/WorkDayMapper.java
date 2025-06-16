package com.web.manage.base.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.web.manage.base.domain.WorkDayVO;

import jakarta.validation.Valid;

@Mapper
public interface WorkDayMapper {
    int getQueryTotalCnt();

    List<HashMap<String, Object>> getMonthDayList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getWorkDayList(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDayInfo(HashMap<String, Object> hashmapParam);

    boolean insertWorkDay( WorkDayVO workDay);
}
