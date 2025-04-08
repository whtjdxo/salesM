package com.web.manage.withdraw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.web.manage.withdraw.service.SubtractService;

import ch.qos.logback.classic.Logger;


import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService; 
import com.web.manage.trans.domain.TransProcessVO;
import jakarta.validation.Valid;

import com.web.common.util.DateUtil;
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
@RequestMapping("/withdraw/subtract/subMng/")
public class SubtractMng {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private SubtractService subtractService;    

   @Autowired
   private CommonService commonService; 

    @RequestMapping("mngview")
    public String view() {
        return "pages/withdraw/subtractMng";
    }

    @RequestMapping("excel")
    public String excelUpload() {
        return "pages/withdraw/vanDocuUpload";
    } 

    
    @RequestMapping("subSummary")    
    public @ResponseBody String getSubSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
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

            list = subtractService.getSubSummary(hashmapParam);
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

    @RequestMapping("chainSubList")    
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
            
            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
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

    @RequestMapping(value = "/insertSubMst", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertSubMst(@ModelAttribute("SubMstVO") @Valid SubMstVO subMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    subMstVo.setEnt_user_id(member.getUserId());
            subMstVo.setSub_no(subtractService.getNewSubNo()); // 신규 생성 번호 

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
                System.out.println("chainCreate fail");
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

    @RequestMapping(value = "/updateSubMst", method = RequestMethod.POST)
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


    // @RequestMapping("list")    
    // public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
    //     HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
    //     List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    //     Gson gson = new Gson();
    //     SessionVO member = (SessionVO) session.getAttribute("S_USER");
    //     hashmapParam.put("user_id", member.getUserId());
    //     String jString = null; 
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);
    //         if (hashmapParam.get("sch_conf_sdt") != null) {
    //             String sch_conf_sdt = commonService.getPreWorkDay();
    //             hashmapParam.put("sch_conf_sdt", sch_conf_sdt);
    //         }
    //         if (hashmapParam.get("sch_conf_edt") != null) {
    //             String sch_conf_edt = commonService.getToDay();
    //             hashmapParam.put("sch_conf_edt", sch_conf_edt);
    //         }

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = subtractService.getScrapDataSumm(hashmapParam);
    //         int records = subtractService.getQueryTotalCnt();

    //         pageing.setRecords(records);
    //         pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

    //         hashmapResult.put("draw", pageing.getDraw());
    //         hashmapResult.put("recordsTotal", pageing.getRecords());
    //         hashmapResult.put("recordsFiltered", pageing.getRecords());
    //         hashmapResult.put("data", list);

    //         jString = gson.toJson(hashmapResult);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return jString;  
    // }

    // @RequestMapping(value = "/getChainDocuSumm", method = RequestMethod.POST)
    // public @ResponseBody String getChainDocuSumm(@RequestBody HashMap<String, Object> hashmapParam) {
    //     HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
    //     List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    //     Gson gson = new Gson();
    //     // System.out.println("getChainDocuSummgetChainDocuSummgetChainDocuSummgetChainDocuSummgetChainDocuSumm");
    //     String jString = null; 
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = subtractService.getChainDocuSumm(hashmapParam);
    //         int records = subtractService.getQueryTotalCnt();

    //         pageing.setRecords(records);
    //         pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

    //         hashmapResult.put("draw", pageing.getDraw());
    //         hashmapResult.put("recordsTotal", pageing.getRecords());
    //         hashmapResult.put("recordsFiltered", pageing.getRecords());
    //         hashmapResult.put("data", list);

    //         jString = gson.toJson(hashmapResult); 
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return jString;  
    // }

    // @RequestMapping(value = "/getChainVanDocuList", method = RequestMethod.POST)
    // public @ResponseBody String getChainVanDocuList(@RequestBody HashMap<String, Object> hashmapParam) {
    //     HashMap<String, Object> hashmapResult = new HashMap<>();
    //     List<HashMap<String, Object>> list = new ArrayList<>();
    //     Gson gson = new Gson();
    //     String jString = null;
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = subtractService.getChainVanDocuList(hashmapParam);
    //         int records = subtractService.getQueryTotalCnt();

    //         pageing.setRecords(records);
    //         pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

    //         hashmapResult.put("draw", pageing.getDraw());
    //         hashmapResult.put("recordsTotal", pageing.getRecords());
    //         hashmapResult.put("recordsFiltered", pageing.getRecords());
    //         hashmapResult.put("data", list);

    //         jString = gson.toJson(hashmapResult);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return jString;
    // }

    // @RequestMapping(value = "/downloadDocuListExcel", method = RequestMethod.POST)
    // public ResponseEntity<byte[]> downloadDocuListExcel(@RequestBody HashMap<String, Object> hashmapParam) {
    //     try {
    //         // Fetch data for the Excel file
    //         hashmapParam.put("sidx", "");
    //         hashmapParam.put("sord", "");
    //         hashmapParam.put("start", "0");
    //         hashmapParam.put("end", "9999");
    //         List<HashMap<String, Object>> list = subtractService.getChainVanDocuList(hashmapParam);

    //         // Create an Excel workbook
    //         Workbook workbook = new XSSFWorkbook();
    //         Sheet sheet = workbook.createSheet("Docu List");

    //         // Create header row
    //         Row headerRow = sheet.createRow(0);
    //         String[] headers = {"일련번호", "VAN", "발급사", "매입사", "카드번호", "카드유형", "구분", "승인번호", "승인일시", "승인금액", "취소", "원거래일", "사전정산", "정산상태", "가맹점명", "가맹점명"};
    //         for (int i = 0; i < headers.length; i++) {
    //             Cell cell = headerRow.createCell(i);
    //             cell.setCellValue(headers[i]);
    //         }
    //         // Populate data rows
    //         int rowIndex = 1;
    //         for (HashMap<String, Object> row : list) {
    //             Row dataRow = sheet.createRow(rowIndex++);
    //             dataRow.createCell(0).setCellValue(String.valueOf(row.get("docu_seq")));
    //             dataRow.createCell(1).setCellValue(String.valueOf(row.get("van_cd_nm")));
    //             dataRow.createCell(2).setCellValue(String.valueOf(row.get("card_iss_nm")));
    //             dataRow.createCell(3).setCellValue(String.valueOf(row.get("card_acq_nm")));
    //             dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_no")));
    //             dataRow.createCell(5).setCellValue(String.valueOf(row.get("card_type_nm")));
    //             dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_gb_nm")));
    //             dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_no")));
    //             dataRow.createCell(8).setCellValue(String.valueOf(row.get("conf_dttm")));
    //             dataRow.createCell(9).setCellValue(String.valueOf(row.get("conf_amt")));
    //             dataRow.createCell(10).setCellValue(String.valueOf(row.get("cncl_yn")));
    //             dataRow.createCell(11).setCellValue(String.valueOf(row.get("org_conf_dt")));
    //             dataRow.createCell(12).setCellValue(String.valueOf(row.get("proc_fg_nm")));
    //             dataRow.createCell(13).setCellValue(String.valueOf(row.get("wd_status_nm")));
    //         }

    //         // Write workbook to a byte array
    //         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //         workbook.write(outputStream);
    //         workbook.close();

    //         // Set response headers
    //         HttpHeaders hHeaders = new HttpHeaders();
    //         hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    //         hHeaders.setContentDispositionFormData("attachment", "DocuList.xlsx");

    //         return ResponseEntity.ok()
    //                 .headers(hHeaders)
    //                 .body(outputStream.toByteArray());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).build();
    //     }
    // }

    // @RequestMapping(value = "/getUnprocessedList", method = RequestMethod.POST)
    // public @ResponseBody String getUnprocessedList(@RequestBody HashMap<String, Object> hashmapParam) {
    //     HashMap<String, Object> hashmapResult = new HashMap<>();
    //     List<HashMap<String, Object>> list = new ArrayList<>();
    //     Gson gson = new Gson();
    //     String jString = null;
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = subtractService.getUnprocessedList(hashmapParam);
    //         int records = subtractService.getQueryTotalCnt();

    //         pageing.setRecords(records);
    //         pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

    //         hashmapResult.put("draw", pageing.getDraw());
    //         hashmapResult.put("recordsTotal", pageing.getRecords());
    //         hashmapResult.put("recordsFiltered", pageing.getRecords());
    //         hashmapResult.put("data", list);

    //         jString = gson.toJson(hashmapResult);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return jString;
    // }

    // @RequestMapping(value = "/getUnprocessedSumm", method = RequestMethod.POST)
    // public @ResponseBody String getUnprocessedSumm(@RequestBody HashMap<String, Object> hashmapParam) {
    //     HashMap<String, Object> hashmapResult = new HashMap<>();
    //     List<HashMap<String, Object>> list = new ArrayList<>();
    //     Gson gson = new Gson();
    //     String jString = null;
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = subtractService.getUnprocessedSumm(hashmapParam);
    //         int records = subtractService.getQueryTotalCnt();

    //         pageing.setRecords(records);
    //         pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

    //         hashmapResult.put("draw", pageing.getDraw());
    //         hashmapResult.put("recordsTotal", pageing.getRecords());
    //         hashmapResult.put("recordsFiltered", pageing.getRecords());
    //         hashmapResult.put("data", list);

    //         jString = gson.toJson(hashmapResult);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return jString;
    // }

    // @RequestMapping(value = "/callScrapTransVanDocu", method = RequestMethod.POST)
    // public @ResponseBody ReturnDataVO callScrapTransVanDocu(@ModelAttribute("TransProcessVO") @Valid TransProcessVO procVo, HttpSession session) {
    //     ReturnDataVO result = new ReturnDataVO();
    //     try {
    //         System.out.println("procVo : " + procVo);
    //         SessionVO member = (SessionVO) session.getAttribute("S_USER");
    //         procVo.setUserId(member.getUserId());
            
    //         return subtractService.callScrapTransVanDocu(procVo);             
    //     } catch (Exception e) {
    //         result.setResultCode("F000");
    //         result.setResultMsg("An error occurred while processing the scrap transaction.");
    //         e.printStackTrace();
    //         return result;
    //     }
    //     // return result;
    // }

    // @SuppressWarnings("deprecation")
    // @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    // public @ResponseBody ReturnDataVO uploadExcel(@RequestParam("file") MultipartFile file) {
    //     ReturnDataVO result = new ReturnDataVO();
    //     List<List<String>> data = new ArrayList<>();
    //     try {
    //         if (file.isEmpty()) {
    //             result.setResultCode("F000");
    //             result.setResultMsg("File is empty.");
    //             return result;
    //         }

    //         Workbook workbook = new XSSFWorkbook(file.getInputStream());
    //         Sheet sheet = workbook.getSheetAt(0);

    //         for (Row row : sheet) {
    //             List<String> rowData = new ArrayList<>();
    //             for (Cell cell : row) {
    //                 cell.setCellType(CellType.STRING);
    //                 rowData.add(cell.getStringCellValue());
    //             }
    //             data.add(rowData);
    //         }
    //         workbook.close();

    //         result.setResultCode("S000");
    //         result.setResultMsg("File processed successfully.");
    //         result.setData(data);
    //     } catch (Exception e) {
    //         result.setResultCode("F000");
    //         result.setResultMsg("An error occurred while processing the file: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    //     return result;
    // }
}

