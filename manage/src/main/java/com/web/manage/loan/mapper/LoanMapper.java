package com.web.manage.loan.mapper;

import java.util.HashMap;
import java.util.List; 

import org.apache.ibatis.annotations.Mapper;
 
import com.web.manage.loan.domain.LoanMstVO;
import com.web.manage.loan.domain.LoanRepayScheduleVO;
import com.web.manage.loan.domain.ProcPrepayVO;
import com.web.manage.loan.domain.ProcSubTransVO;

@Mapper
public interface LoanMapper {
    int getQueryTotalCnt(); 
    List<HashMap<String, Object>> getCorpList(HashMap<String, Object> hashmapParam);    
    List<HashMap<String, Object>> getLoanSummary(HashMap<String, Object> hashmapParam);    
    HashMap<String, Object> getLoanSummTotal(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getChainLoanList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getChainLoanListTotal(HashMap<String, Object> hashmapParam);

    HashMap<String, Object> getChainLoanInfo(HashMap<String, Object> hashmapParam);
    List<HashMap<String, Object>> getLoanRepaymentList(HashMap<String, Object> hashmapParam);

    List<HashMap<String, Object>> getLoanRecvLogList(HashMap<String, Object> hashmapParam);
    HashMap<String, Object> getLoanRecvLogTotal  (HashMap<String, Object> hashmapParam);

    String getNewLoanNo();
    boolean insertLoanMst(LoanMstVO loanMstVo );

    HashMap<String, Object> changeLoanChk(LoanMstVO loanMstVo);
    boolean updateLoanMst(LoanMstVO loanMstVo );    

    boolean deleteLoanMst(LoanMstVO loanMstVo );
 
    HashMap<String, Object> loanDeleteChk(LoanMstVO loanMstVo);

    boolean insertLoanRepaySchedule(LoanRepayScheduleVO repaySchedule);
    boolean deleteLoanRepaySchedule(LoanMstVO loanMstVo);
    void callProcLoanPrepay(ProcPrepayVO procVo); 

    void callProcLoanSendSubTrans(ProcSubTransVO procVo);
}
