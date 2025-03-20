package com.web.manage.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.base.mapper.WorkDayMapper;

@Service
public class WorkDayService {

    @Autowired
    private WorkDayMapper workDayMapper;

    public List<HashMap<String, Object>> getWorkDayList(HashMap<String, Object> hashmapParam) {
        return workDayMapper.getWorkDayList(hashmapParam);
    }

    public boolean insertWorkDay(HashMap<String, Object> workDayData) {
        return workDayMapper.insertWorkDay(workDayData);
    }
}
