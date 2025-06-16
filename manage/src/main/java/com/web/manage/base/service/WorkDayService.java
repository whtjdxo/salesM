package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.web.manage.base.domain.WorkDayVO;
import com.web.manage.base.mapper.WorkDayMapper;

import jakarta.validation.Valid;

@Service
public class WorkDayService {

    @Autowired
    private WorkDayMapper workDayMapper;

    public int getQueryTotalCnt() {
        return workDayMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getMonthDayList(HashMap<String, Object> hashmapParam) {
        return workDayMapper.getMonthDayList(hashmapParam);
    }

    public List<HashMap<String, Object>> getWorkDayList(HashMap<String, Object> hashmapParam) {
        return workDayMapper.getWorkDayList(hashmapParam);
    }

    public HashMap<String, Object> getDayInfo(HashMap<String, Object> hashmapParam) {
        return workDayMapper.getDayInfo(hashmapParam);
    }

    public boolean insertWorkDay( WorkDayVO workDayVo) {
        return workDayMapper.insertWorkDay(workDayVo);
    }
}
