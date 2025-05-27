package com.web.manage.deposit.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.deposit.domain.ProcDepositVO;

@Mapper
public interface DepositMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDepositSummaryTotal(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getChainDepoStatus(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoResvList(HashMap<String, Object> hashmapParam);
    void callProcChangeResvDate(ProcDepositVO procVo);
    void callProcDepositAdjust(ProcDepositVO procVo);

    List<HashMap<String, Object>> getDepoAdjustSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDepoAdjustSummTotal(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoAdjustCardSummary(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getDepoAdjustList(HashMap<String, Object> hashmapParam);

    void callProcDepoAdjustCancel(ProcDepositVO procVo);
}