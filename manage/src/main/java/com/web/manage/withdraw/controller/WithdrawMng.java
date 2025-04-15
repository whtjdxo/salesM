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

import com.web.manage.withdraw.service.WithdrawService;

import ch.qos.logback.classic.Logger;


import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.web.manage.trans.domain.MapCodeVO;
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
@RequestMapping("/withdraw/withdraw/")

public class WithdrawMng {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private WithdrawService withdrawService; 

    @RequestMapping("wdMng/view")
    public String view() {
        return "pages/withdraw/withdrawMng";
    }

    // @RequestMapping("excel") 
    // public String excelUpload() {
    //     return "pages/withdraw/vanDocuUpload";
    // } 
 

    @RequestMapping("wdMng/wdSummary")    
    public @ResponseBody String getWDSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);

            // System.out.println(hashmapParam);

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getWDSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

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

    @RequestMapping("wdMng/wdChainSummary")    
    public @ResponseBody String getWdChainSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);

            // System.out.println(hashmapParam);

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getWDChainSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

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

    @RequestMapping("wdMng/wdCardSummary")    
    public @ResponseBody String getWDCardSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);

            // System.out.println(hashmapParam);

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getWDCardSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

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

    @RequestMapping("wdMng/wdResvList")    
    public @ResponseBody String getWdResvlList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            list = withdrawService.getWdResvlList(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

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

    @RequestMapping(value = "wdMng/wdResvListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> wdResvListExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = withdrawService.getWdResvlList(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                  "정산 번호"       , "정산 상태"       , "매입사"          ,  "카드번호"       , "카드유형"
                , "구분"           , "승인번호"        , "승인일시"         , "입금예정일"       , "승인금액"
                , "카드수수료"      , "입금예정액"       , "서비스수수료"      , "정산 원금"       , "여신수수료"
                , "출금예정액"      , "비고"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            // Populate data rows
            int rowIndex = 1;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("wd_no")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("wd_status_nm")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("card_acq_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("card_no")));
                dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_type_nm")));
                dataRow.createCell(5).setCellValue(String.valueOf(row.get("conf_gb_nm")));
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_no")));
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_dttm")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("card_resv_date")));
                dataRow.createCell(9).setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                dataRow.createCell(10).setCellValue(Double.parseDouble(String.valueOf(row.get("card_fee_amt"))));
                dataRow.createCell(11).setCellValue(Double.parseDouble(String.valueOf(row.get("card_resv_amt"))));
                dataRow.createCell(12).setCellValue(Double.parseDouble(String.valueOf(row.get("svc_fee_amt"))));
                dataRow.createCell(13).setCellValue(Double.parseDouble(String.valueOf(row.get("wd_base_amt"))));
                dataRow.createCell(14).setCellValue(Double.parseDouble(String.valueOf(row.get("crd_fee_amt"))));
                dataRow.createCell(15).setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                dataRow.createCell(16).setCellValue(String.valueOf(row.get("wd_memo")));
            }

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