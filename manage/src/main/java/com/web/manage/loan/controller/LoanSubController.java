package com.web.manage.loan.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.SessionVO;

import com.web.manage.loan.service.LoanSubService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/loan/loan/")
public class LoanSubController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private LoanSubService loanSubService;  

    @RequestMapping("loanSubList/view")    
    public String view() {
        return "pages/loan/loanSubList";
    } 
 
    @RequestMapping("loanSubList/loanSubSummary")    
    public @ResponseBody String getSubSummaryList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) throws Exception {
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
 
            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                Object orderObj = pageing.getOrder();  // 타입이 List 또는 Map 둘 다 가능하다고 가정
                List<Map<String, Object>> orderList = new ArrayList<>();
                if (orderObj instanceof List) {
                    // 다중 정렬
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> tempList = (List<Map<String, Object>>) orderObj;
                    orderList = tempList;
                } else if (orderObj instanceof Map) {
                    // 단일 정렬 → List로 감싸주기
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tempMap = (Map<String, Object>) orderObj;
                    orderList.add(tempMap);
                }  

                StringBuilder orderBy = new StringBuilder();

                for (Map<String, Object> ord : orderList) {
                    int colIdx = Integer.parseInt(String.valueOf(ord.get("column")));
                    String colName = String.valueOf(pageing.getColumns().get(colIdx).get("data")) ;
                    String direction = String.valueOf(ord.get("dir"));

                    if (orderBy.length() > 0) {
                        orderBy.append(", ");
                    }
                    orderBy.append(colName).append(" ").append(direction);
                }

                if (orderBy.length() == 0) {
                    orderBy.append("1"); // 기본 정렬
                }

                hashmapParam.put("orderBy", orderBy.toString());
            } else {
                hashmapParam.put("orderBy", "");    
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = loanSubService.getLoanSubSummary(hashmapParam);
            int records = loanSubService.getQueryTotalCnt();
            totalSumm = loanSubService.getLoanSubSummaryTotal(hashmapParam);

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

    @RequestMapping("loanSubList/chainLoanSubList")    
    public @ResponseBody String  getChainLoanSubList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) throws Exception {
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
 
            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                Object orderObj = pageing.getOrder();  // 타입이 List 또는 Map 둘 다 가능하다고 가정
                List<Map<String, Object>> orderList = new ArrayList<>();
                if (orderObj instanceof List) {
                    // 다중 정렬
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> tempList = (List<Map<String, Object>>) orderObj;
                    orderList = tempList;
                } else if (orderObj instanceof Map) {
                    // 단일 정렬 → List로 감싸주기
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tempMap = (Map<String, Object>) orderObj;
                    orderList.add(tempMap);
                }  

                StringBuilder orderBy = new StringBuilder();

                for (Map<String, Object> ord : orderList) {
                    int colIdx = Integer.parseInt(String.valueOf(ord.get("column")));
                    String colName = String.valueOf(pageing.getColumns().get(colIdx).get("data")) ;
                    String direction = String.valueOf(ord.get("dir"));

                    if (orderBy.length() > 0) {
                        orderBy.append(", ");
                    }
                    orderBy.append(colName).append(" ").append(direction);
                }

                if (orderBy.length() == 0) {
                    orderBy.append("1"); // 기본 정렬
                }

                hashmapParam.put("orderBy", orderBy.toString());
            } else {
                hashmapParam.put("orderBy", "");    
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = loanSubService.getChainLoanSubList(hashmapParam);
            int records = loanSubService.getQueryTotalCnt(); 
            totalSumm = loanSubService.getChainLoanSubListTotal(hashmapParam);
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
}
