package com.web.manage.deposit.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepositMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepositList(HashMap<String, Object> hashmapParam);
    
    boolean processDeposit(HashMap<String, Object> hashmapParam);
}