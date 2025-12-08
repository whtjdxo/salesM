package com.web.manage.deposit.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.deposit.domain.DepositExcelRowDataVO;
import com.web.manage.deposit.domain.ProcDepositVO;
import com.web.manage.deposit.domain.ProcTransDepositVO;

@Mapper
public interface DepositMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getDepositSummaryTotal(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getChainDepoStatus(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getDepoResvList(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getSearchConfDate(HashMap<String, String> hashmapParam);

    HashMap<String, Object> getDepositConfDate(HashMap<String, String> hashmapParam);

    void callProcChangeResvDate(ProcDepositVO procVo);
    void callProcDepositAdjust(ProcDepositVO procVo);
    List<HashMap<String, Object>> getDepoAdjustSummary(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getDepoAdjustSummTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getChainDepositList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getChainMnulDepositList(HashMap<String, Object> hashmapParam);

    String getNewChainDepositNo();   
    String getNewCreditDepositNo();   
    HashMap<String, Object> getChainDepositTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getCreditDepositList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getCreditDepositTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getDepoAdjustCardSummary(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getDepoAdjustList(HashMap<String, Object> hashmapParam);

    void callProcDepoAdjustCancel(ProcDepositVO procVo);

    String getCorpAccountNo(HashMap<String, Object> hashmapParam);

    boolean excelUploadBankData(DepositExcelRowDataVO excelRowdataVo);
    void callProcTransDepositBatch(ProcTransDepositVO procTransDepositVo);
}