package com.web.manage.loan.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.web.manage.loan.domain.LoanMstVO;
import com.web.manage.loan.domain.LoanRepayScheduleVO;
import com.web.manage.loan.domain.ProcPrepayVO;
import com.web.manage.loan.service.LoanService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/loan/loan/")
public class LoanController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    @Autowired
    private LoanService loanService;

    @Autowired
    private CommonService commonService; 

    @RequestMapping("loanMng/view")    
    public String view() {
        return "pages/loan/loanMng";
    }
 
 
    @RequestMapping("loanMng/loanSummary")    
    public @ResponseBody String getLoanSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm     = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
        hashmapParam.put("userCorpType", member.getUserCorpType());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam); 
 
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = loanService.getLoanSummary(hashmapParam);
            int records = loanService.getQueryTotalCnt();
            totalSumm = loanService.getLoanSummTotal(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("totalSumm", totalSumm);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping("loanMng/chainLoanList")    
    public @ResponseBody String getChainLoanList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm     = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam); 
 
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = loanService.getChainLoanList(hashmapParam);
            int records = loanService.getQueryTotalCnt();
            totalSumm = loanService.getChainLoanListTotal(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("totalSumm", totalSumm);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

	@RequestMapping("loanMng/viewRepaySchdule")    
    public @ResponseBody String getViewRepaySchedule(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {

			String loan_no = (String) hashmapParam.get("loan_no"); 
			if ("".equals(loan_no))	{
                String loanType = (String) hashmapParam.get("loan_type");
                BigDecimal loanPrincAmt = new BigDecimal(((String) hashmapParam.get("princ_amt")).replace(",", ""));
                // BigDecimal 처리 방법 1: String인 경우
                BigDecimal intRate;
                Object intRateObj = hashmapParam.get("int_rate");
                if (intRateObj instanceof String) {
                    intRate = new BigDecimal(((String) intRateObj).replace(",", ""));
                } else if (intRateObj instanceof BigDecimal) {
                    intRate = (BigDecimal) intRateObj;
                } else {
                    // 다른 타입이 들어올 경우 기본값 설정 또는 예외 처리
                    intRate = BigDecimal.ZERO; // 또는 throw new IllegalArgumentException("Invalid interest rate type");
                }
                // int 처리 방법
                int loanDays;
                Object loanDaysObj = hashmapParam.get("loan_day");
                if (loanDaysObj instanceof Integer) {
                    loanDays = (int) loanDaysObj;
                } else if (loanDaysObj instanceof String) {
                    loanDays = Integer.parseInt((String) loanDaysObj);
                } else {
                    // 기본값 설정 또는 예외 처리
                    loanDays = 0; // 또는 throw new IllegalArgumentException("Invalid loan days type");
                }

                // LocalDate 처리 방법
                LocalDate loanSDate;
                Object loanSDateObj = hashmapParam.get("loan_sdt");
                if (loanSDateObj instanceof LocalDate) {
                    loanSDate = (LocalDate) loanSDateObj;
                } else if (loanSDateObj instanceof String) {
                    loanSDate = LocalDate.parse((String) loanSDateObj, DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    // 기본값 설정 또는 예외 처리
                    loanSDate = LocalDate.now(); // 또는 throw new IllegalArgumentException("Invalid loan start date type");
                }
				List<LoanRepayScheduleVO> list  = new ArrayList<LoanRepayScheduleVO>();	
            	list = loanService.getLoanRepaymentVOs(loanType, loanPrincAmt, intRate, loanDays, loanSDate);
				hashmapResult.put("data", list); 
			} else {
				List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
				list = loanService.getLoanRepaymentList(hashmapParam);
				hashmapResult.put("data", list); 
			} 

            jString = gson.toJson(hashmapResult);
			
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

	@RequestMapping(value = "loanMng/insertLoanMst", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertLoanMst(@ModelAttribute("LoanMstVO") @Valid LoanMstVO loanMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
		try {
			SessionVO member = (SessionVO) session.getAttribute("S_USER");
			loanMstVo.setEnt_user_id(member.getUserId());
			loanMstVo.setPrinc_amt(loanMstVo.getPrinc_amt().replace(",", ""));
            loanMstVo.setInt_amt(loanMstVo.getInt_amt().replace(",", ""));
            loanMstVo.setTot_loan_amt(loanMstVo.getTot_loan_amt().replace(",", ""));
			loanMstVo.setLoan_no(loanService.getNewLoanNo());

			if (loanService.insertLoanMst(loanMstVo)) {					 
				result.setResultCode("S000");
				result.setResultMsg("LoanMst and Repay Schedule creation successful.");
			} else {
				result.setResultCode("F000");
				result.setResultMsg("LoanMst Amt creation Failed");
			}
		} catch (Exception e) {
			result.setResultCode("F000");
			result.setResultMsg("LoanMst Amt creation Failed");
			e.printStackTrace();
		}
        return result;
    }

    @RequestMapping(value = "loanMng/updateLoanMst", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateLoanMst(@ModelAttribute("LoanMstVO") @Valid LoanMstVO loanMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    loanMstVo.setUpt_user_id(member.getUserId());             
            if (loanService.updateLoanMst(loanMstVo)) {
                System.out.println("Update LoanMst  success");
                result.setResultCode("S000");
                result.setResultMsg("LoanMst Update successful.");     
                
            } else {
                System.out.println("UpdateExceed  Fail");
                result.setResultCode("F000");
                result.setResultMsg("LoanMst update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("LoanMst update failed.");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "loanMng/procLoanPrepay", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcLoanPrepay(@ModelAttribute("ProcPrepayVO") @Valid ProcPrepayVO procVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    procVo.setUserId(member.getUserId());

            if (loanService.callProcLoanPrepay(procVo)) {
                System.out.println("PrePay Change success");
                result.setResultCode("S000");
                result.setResultMsg("PrePay Update successful.");     
                
            } else {
                System.out.println("PrePay  Fail");
                result.setResultCode("F000");
                result.setResultMsg("PrePay update failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("PrePay update failed.");
            e.printStackTrace();
        }
        return result;
    }
 

    @RequestMapping(value = "loanMng/deleteLoanMst", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO deleteLoanMst(@ModelAttribute("LoanMstVO") @Valid LoanMstVO loanMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    loanMstVo.setUpt_user_id(member.getUserId());  

            if (loanService.deleteLoanMst(loanMstVo)) {
                System.out.println("Delete LoanMst  success");
                result.setResultCode("S000");
                result.setResultMsg("LoanMst Delete successful.");
            } else {
                System.out.println("Delete Loan Master Fail");
                result.setResultCode("F000");
                result.setResultMsg("LoanMst Delete failed.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("LoanMst Delete failed.");
            e.printStackTrace();
        }
        return result;
    }
	

}
