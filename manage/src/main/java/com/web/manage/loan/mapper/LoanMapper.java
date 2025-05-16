package com.web.manage.loan.mapper;

import java.util.HashMap;
import java.util.List; 

import org.apache.ibatis.annotations.Mapper;
 
import com.web.manage.loan.domain.LoanMstVO;
import com.web.manage.loan.domain.LoanRepayScheduleVO;

@Mapper
public interface LoanMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getLoanSummary(HashMap<String, Object> hashmapParam);    
    HashMap<String, Object> getLoanSummTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getChainLoanList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getChainLoanListTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getLoanRepaymentList(HashMap<String, Object> hashmapParam);
    
    boolean insertLoanMst(LoanMstVO loanMstVo );
    boolean updateLoanMst(LoanMstVO loanMstVo );

    boolean insertLoanRepaySchedule(LoanRepayScheduleVO repaySchedule);
}
