package com.web.manage.students.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {

    List<HashMap<String, Object>> studentsListRetrieve(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    boolean studentsCreate(HashMap<String, Object> hashmapParam);
    boolean studentsUpdate(HashMap<String, Object> hashmapParam);

    int stdIdCheck(HashMap<String, Object> hashmapParam);
}