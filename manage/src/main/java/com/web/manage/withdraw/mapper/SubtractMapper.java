package com.web.manage.withdraw.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
 
import com.web.manage.withdraw.domain.SubMstVO;
import com.web.manage.withdraw.domain.ProcSubReceiveVO;

@Mapper
public interface SubtractMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getSubSummary(HashMap<String, Object> hashmapParam);    
    List<HashMap<String, Object>> getChainSubList(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getSubReceiveList(HashMap<String, Object> hashmapParam);    

    String getNewSubNo();    
    boolean insertSubMst(SubMstVO      subMstVo);
    boolean updateSubMst(SubMstVO      subMstVo);

    void callProcSubReveive(ProcSubReceiveVO subReceiveVO);    

    List<HashMap<String, Object>> getSubStatSummary(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getSubStatDetail(HashMap<String, Object> hashmapParam);    
}

