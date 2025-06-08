package com.web.manage.deposit.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.web.manage.deposit.domain.ProcDayCloseVO; 

@Mapper
public interface DayCloseMapper {
    int getQueryTotalCnt(); 
    int getDayCloseCheck(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getDayPreCloseList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getDayPreCloseSumm(HashMap<String, Object> hashmapParam);
    
    List<HashMap<String, Object>> getDayCloseList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getDayCloseSumm(HashMap<String, Object> hashmapParam);

    void callProcDayClose(ProcDayCloseVO procVo);

}
