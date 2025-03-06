package com.web.manage.students.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.students.mapper.StudentMapper;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public List<HashMap<String, Object>> studentsListRetrieve(HashMap<String, Object> hashmapParam) {
        return studentMapper.studentsListRetrieve(hashmapParam);
    }
    public int getQueryTotalCnt() {
        return studentMapper.getQueryTotalCnt();
    }
    public boolean studentsCreate(HashMap<String, Object> hashmapParam) {
        return studentMapper.studentsCreate(hashmapParam);
    }

    public boolean studentsUpdate(HashMap<String, Object> hashmapParam) {
        return studentMapper.studentsUpdate(hashmapParam);
    }
    public int stdIdCheck(HashMap<String, Object> hashmapParam) {
        return studentMapper.stdIdCheck(hashmapParam);
    }
}
