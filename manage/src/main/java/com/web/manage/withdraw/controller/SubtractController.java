package com.web.manage.withdraw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.withdraw.domain.SubMstVO;
import com.web.manage.withdraw.domain.ProcSubReceiveVO;
import com.web.manage.withdraw.service.SubtractService;

import ch.qos.logback.classic.Logger;


import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService; 
import com.web.manage.trans.domain.TransProcessVO;
import jakarta.validation.Valid;

import com.web.common.util.DateUtil;
import com.web.common.util.ExcelStyleUtil;
import com.web.common.util.StringUtil;
import com.web.common.util.ValidateUtil;
import com.web.config.interceptor.AuthInterceptor;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Controller
@RequestMapping("/withdraw/subtract/")
public class SubtractController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private SubtractService subtractService;    

   @Autowired
   private CommonService commonService; 

    @RequestMapping("subMng/view")
    public String view() {
        return "pages/withdraw/subtractMng";
    }
 
    @RequestMapping("subMng/subSummary")    
    public @ResponseBody String getSubSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm      = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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

            list = subtractService.getSubSummary(hashmapParam);
            int records = subtractService.getQueryTotalCnt();
            totalSumm = subtractService.getSubSummaryTotal(hashmapParam);

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

    @RequestMapping("subMng/chainSubList")    
    public @ResponseBody String getChainSubList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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

            list = subtractService.getChainSubList(hashmapParam);
            int records = subtractService.getQueryTotalCnt();

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping("subMng/chainWdSubList")    
    public @ResponseBody String getChainWdSubList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = subtractService.getChainWdSubList(hashmapParam);
            int records = subtractService.getQueryTotalCnt();

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping("subMng/subReceiveList")    
    public @ResponseBody String getSubReceiveList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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
            list = subtractService.getSubReceiveList(hashmapParam);
            int records = subtractService.getQueryTotalCnt();

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    } 

    @RequestMapping(value = "subMng/insertSubMst", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertSubMst(@ModelAttribute("SubMstVO") @Valid SubMstVO subMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    subMstVo.setEnt_user_id(member.getUserId());
            subMstVo.setSub_no(commonService.getJobSeq("TB_SUB_MST", "SUB_NO")); // 신규 생성 번호 

            subMstVo.setOccur_crd_amt(subMstVo.getOccur_crd_amt() == null ? "0" : subMstVo.getOccur_crd_amt().replaceAll(",", ""));
            subMstVo.setOccur_svc_amt(subMstVo.getOccur_svc_amt() == null ? "0" : subMstVo.getOccur_svc_amt().replaceAll(",", ""));
            subMstVo.setOccur_base_amt(subMstVo.getOccur_base_amt() == null ? "0" : subMstVo.getOccur_base_amt().replaceAll(",", ""));
            subMstVo.setOccur_amt(subMstVo.getOccur_amt() == null ? "0" : subMstVo.getOccur_amt().replaceAll(",", ""));

            subMstVo.setRecv_crd_amt(subMstVo.getRecv_crd_amt() == null ? "0" : subMstVo.getRecv_crd_amt().replaceAll(",", ""));
            subMstVo.setRecv_svc_amt(subMstVo.getRecv_svc_amt() == null ? "0" : subMstVo.getRecv_svc_amt().replaceAll(",", ""));
            subMstVo.setRecv_base_amt(subMstVo.getRecv_base_amt() == null ? "0" : subMstVo.getRecv_base_amt().replaceAll(",", ""));
            subMstVo.setRecv_amt(subMstVo.getRecv_amt() == null ? "0" : subMstVo.getRecv_amt().replaceAll(",", ""));
            
            subMstVo.setRemain_crd_amt(subMstVo.getRemain_crd_amt() == null ? "0" : subMstVo.getRemain_crd_amt().replaceAll(",", ""));
            subMstVo.setRemain_svc_amt(subMstVo.getRemain_svc_amt() == null ? "0" : subMstVo.getRemain_svc_amt().replaceAll(",", ""));
            subMstVo.setRemain_base_amt(subMstVo.getRemain_base_amt() == null ? "0" : subMstVo.getRemain_base_amt().replaceAll(",", ""));
            subMstVo.setRemain_amt(subMstVo.getRemain_amt() == null ? "0" : subMstVo.getRemain_amt().replaceAll(",", ""));

            if (subtractService.insertSubMst(subMstVo)) {
                System.out.println("Subtract Create success");
                result.setResultCode("S000");
                result.setResultMsg("Subtract creation successful.");
            } else {
                // System.out.println("Subtract Create fail");
                result.setResultCode("F000");
                result.setResultMsg("Subtract creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Subtract creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "subMng/updateSubMst", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateSubMst(@ModelAttribute("SubMstVO") @Valid SubMstVO subMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");            
    	    subMstVo.setUpt_user_id(member.getUserId()); 
            
            subMstVo.setOccur_crd_amt(subMstVo.getOccur_crd_amt() == null ? "0" : subMstVo.getOccur_crd_amt().replaceAll(",", ""));
            subMstVo.setOccur_svc_amt(subMstVo.getOccur_svc_amt() == null ? "0" : subMstVo.getOccur_svc_amt().replaceAll(",", ""));
            subMstVo.setOccur_base_amt(subMstVo.getOccur_base_amt() == null ? "0" : subMstVo.getOccur_base_amt().replaceAll(",", ""));
            subMstVo.setOccur_amt(subMstVo.getOccur_amt() == null ? "0" : subMstVo.getOccur_amt().replaceAll(",", ""));

            subMstVo.setRecv_crd_amt(subMstVo.getRecv_crd_amt() == null ? "0" : subMstVo.getRecv_crd_amt().replaceAll(",", ""));
            subMstVo.setRecv_svc_amt(subMstVo.getRecv_svc_amt() == null ? "0" : subMstVo.getRecv_svc_amt().replaceAll(",", ""));
            subMstVo.setRecv_base_amt(subMstVo.getRecv_base_amt() == null ? "0" : subMstVo.getRecv_base_amt().replaceAll(",", ""));
            subMstVo.setRecv_amt(subMstVo.getRecv_amt() == null ? "0" : subMstVo.getRecv_amt().replaceAll(",", ""));
            
            subMstVo.setRemain_crd_amt(subMstVo.getRemain_crd_amt() == null ? "0" : subMstVo.getRemain_crd_amt().replaceAll(",", ""));
            subMstVo.setRemain_svc_amt(subMstVo.getRemain_svc_amt() == null ? "0" : subMstVo.getRemain_svc_amt().replaceAll(",", ""));
            subMstVo.setRemain_base_amt(subMstVo.getRemain_base_amt() == null ? "0" : subMstVo.getRemain_base_amt().replaceAll(",", ""));
            subMstVo.setRemain_amt(subMstVo.getRemain_amt() == null ? "0" : subMstVo.getRemain_amt().replaceAll(",", ""));

            if (subtractService.updateSubMst(subMstVo)) {
                System.out.println("Chain Subtract Update  success");
                result.setResultCode("S000");
                result.setResultMsg("Subtract Update successful.");
            } else {
                System.out.println("Chain Subtract Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("Subtract Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("Subtract Update Failed");
            e.printStackTrace();
        }
        return result;
    }
 

    @RequestMapping(value = "subMng/callProcSubReceive", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcSubReceive(@ModelAttribute("ProcSubReceiveVO") @Valid ProcSubReceiveVO procSRVo, HttpSession session) {
        
        ReturnDataVO result = new ReturnDataVO();
        // procSRVo.setRecvAmt(procSRVo.getRecvAmt().replace(",",""));    // 숫자, 제거 

        System.out.println(procSRVo);
        
        try {
            System.out.println("procVo : " + procSRVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procSRVo.setUserId(member.getUserId());
            
            return subtractService.callProcSubReveive(procSRVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        } 
    }

    @RequestMapping("subStat/view")
    public String statView() {
        return "pages/withdraw/subStat";
    } 

    @RequestMapping("subStat/subStatSummary")    
    public @ResponseBody String getSubStatSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm      = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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

            list = subtractService.getSubStatSummary(hashmapParam);
            int records = subtractService.getQueryTotalCnt();

            totalSumm = subtractService.getSubStatSummaryTotal(hashmapParam);

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

    @RequestMapping("subStat/subStatDetail")    
    public @ResponseBody String getSubStatDetail(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
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

            list = subtractService.getSubStatDetail(hashmapParam);
            int records = subtractService.getQueryTotalCnt();

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping(value = "subStat/downExcelSubSumm", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downExcelSubSumm(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = subtractService.getSubStatSummary(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("차감현황");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("차감현황");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "가맹점 명", "사업자번호",
                "출금차감 건", "출금차감 발생금액", "출금차감 차감금액", "출금차감 미차감액",
                "입금차감 건", "입금차감 발생금액", "입금차감 차감금액", "입금차감 미차감액",
                "대출차감 건", "대출차감 발생금액", "대출차감 차감금액", "대출차감 미차감액",
                "수기차감 건", "수기차감 발생금액", "수기차감 차감금액", "수기차감 미차감액",
                "기타차감 건", "기타차감 발생금액", "기타차감 차감금액", "기타차감 미차감액",
                "총계 건", "총계 발생금액", "총계 차감금액", "총계 미차감액"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            // Populate data rows
            int rowIndex = 2;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("chain_nm")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("biz_no")));                
                
                // dataRow.createCell(2).setCellValue(String.valueOf(row.get("count_o")));
                Cell cell = dataRow.createCell(2);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("count_o"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(3).setCellValue(String.valueOf(row.get("occur_amt_o")));
                cell = dataRow.createCell(3);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_o"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(4).setCellValue(String.valueOf(row.get("recv_amt_o")));
                cell = dataRow.createCell(4);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_o"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(5).setCellValue(String.valueOf(row.get("remain_amt_o")));
                cell = dataRow.createCell(5);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_o"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(6).setCellValue(String.valueOf(row.get("count_i")));
                cell = dataRow.createCell(6);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("count_i"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(7).setCellValue(String.valueOf(row.get("occur_amt_i")));
                cell = dataRow.createCell(7);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_i"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(8).setCellValue(String.valueOf(row.get("recv_amt_i")));
                cell = dataRow.createCell(8);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_i"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(9).setCellValue(String.valueOf(row.get("remain_amt_i")));
                cell = dataRow.createCell(9);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_i"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(10).setCellValue(String.valueOf(row.get("count_l")));
                cell = dataRow.createCell(10);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("count_l"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(11).setCellValue(String.valueOf(row.get("occur_amt_l")));
                cell = dataRow.createCell(11);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_l"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(12).setCellValue(String.valueOf(row.get("recv_amt_l")));
                cell = dataRow.createCell(12);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_l"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(13).setCellValue(String.valueOf(row.get("remain_amt_l")));
                cell = dataRow.createCell(13);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_l"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(14).setCellValue(String.valueOf(row.get("count_m")));
                cell = dataRow.createCell(14);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("count_m"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(15).setCellValue(String.valueOf(row.get("occur_amt_m")));
                cell = dataRow.createCell(15);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_m"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(16).setCellValue(String.valueOf(row.get("recv_amt_m")));
                cell = dataRow.createCell(16);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_m"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(17).setCellValue(String.valueOf(row.get("remain_amt_m")));
                cell = dataRow.createCell(17);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_m"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(18).setCellValue(String.valueOf(row.get("count_etc")));
                cell = dataRow.createCell(18);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("count_etc"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(19).setCellValue(String.valueOf(row.get("occur_amt_etc")));
                cell = dataRow.createCell(19);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_etc"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(20).setCellValue(String.valueOf(row.get("recv_amt_etc")));
                cell = dataRow.createCell(20);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_etc"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(21).setCellValue(String.valueOf(row.get("remain_amt_etc")));
                cell = dataRow.createCell(21);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_etc"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(22).setCellValue(String.valueOf(row.get("tot_cnt")));
                cell = dataRow.createCell(22);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_cnt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(23).setCellValue(String.valueOf(row.get("occur_amt_tot")));
                cell = dataRow.createCell(23);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("occur_amt_tot"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(24).setCellValue(String.valueOf(row.get("recv_amt_tot")));
                cell = dataRow.createCell(24);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_amt_tot"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(25).setCellValue(String.valueOf(row.get("remain_amt_tot")));
                cell = dataRow.createCell(25);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt_tot"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
            }
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 25);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "subsummary.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}

