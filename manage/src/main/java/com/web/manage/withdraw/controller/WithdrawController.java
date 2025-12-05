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

import com.web.manage.withdraw.domain.ProcRemitVO;
import com.web.manage.withdraw.domain.SubMstVO;
import com.web.manage.withdraw.service.WithdrawService;

import ch.qos.logback.classic.Logger;

import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import jakarta.validation.Valid;

import com.web.common.util.DateUtil;
import com.web.common.util.ExcelStyleUtil;
import com.web.common.util.StringUtil;
import com.web.common.util.ValidateUtil;
import com.web.config.interceptor.AuthInterceptor;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference; 

@Controller
@RequestMapping("/withdraw/withdraw/")

public class WithdrawController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private WithdrawService withdrawService; 

    @RequestMapping("wdMng/view")
    public String wdView() {
        return "pages/withdraw/withdrawMng";
    } 

    @RequestMapping("remitMng/view")
    public String remitView() {
        return "pages/withdraw/remitMng";
    }

    @RequestMapping("unRemit/view")
    public String unRemitView() {
        return "pages/withdraw/unRemitList";
    }

    @RequestMapping("wdMng/wdSummary")    
    public @ResponseBody String getWDSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getWDSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();
            
            totalSumm= withdrawService.getWDSummaryTotal(hashmapParam);

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

    @RequestMapping(value="wdMng/totWdSummary")
	public @ResponseBody ReturnDataVO getTotWDSummary(@RequestParam HashMap<String, Object> hashmapParam, HttpSession session){
		
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
        HashMap<String, Object> totalSumm      = new HashMap<String, Object>();
		// System.out.println("※ ※ ※ ※ ※ ※ ※ ※ hashmapParam : " + hashmapParam);
		ReturnDataVO result = new ReturnDataVO();
		try {
			totalSumm = withdrawService.getWDSummaryTotal(hashmapParam);
			result.setResultCode("S000");
			result.setData(totalSumm);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
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

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
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

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
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
        System.out.println("sch_list_wd_status type: " + hashmapParam.get("sch_list_wd_status").getClass());
        System.out.println(hashmapParam.get("sch_list_wd_status"));

        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam); 

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
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
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("정산 상세 리스트");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                  "정산 번호"       , "정산 상태"       , "매입사"          , "VAN"             ,  "카드번호"       , "카드유형"
                , "구분"           , "승인번호"        , "승인일시"         , "입금예정일"       , "승인금액"
                , "카드수수료"      , "입금예정액"       , "서비스수수료"     , "정산 원금"       , "여신수수료"
                , "출금예정액"      , "비고"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(excelStyle.getStyle("header"));
                // 각 컬럼 너비 자동 조정
                sheet.autoSizeColumn(i);                
                // 한글의 경우 autoSizeColumn이 완벽하지 않을 수 있어 약간의 여백 추가
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }
            // Populate data rows
            int rowIndex = 2;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("wd_no")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("wd_status_nm")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("card_acq_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("van_cd_nm")));
                dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_no")));
                dataRow.createCell(5).setCellValue(String.valueOf(row.get("card_type_nm")));
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_gb_nm")));
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_no")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("conf_dttm")));
                dataRow.createCell(9).setCellValue(String.valueOf(row.get("card_resv_date")));
                
                Cell c10 = dataRow.createCell(10);
                c10.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                c10.setCellStyle(excelStyle.getStyle("number"));
                
                Cell c11 = dataRow.createCell(11);
                c11.setCellValue(Double.parseDouble(String.valueOf(row.get("card_fee_amt"))));
                c11.setCellStyle(excelStyle.getStyle("number"));

                Cell c12 = dataRow.createCell(12);
                c12.setCellValue(Double.parseDouble(String.valueOf(row.get("card_resv_amt"))));
                c12.setCellStyle(excelStyle.getStyle("number"));

                Cell c13 = dataRow.createCell(13);
                c13.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_fee_amt"))));
                c13.setCellStyle(excelStyle.getStyle("number"));

                Cell c14 = dataRow.createCell(14);
                c14.setCellValue(Double.parseDouble(String.valueOf(row.get("wd_base_amt"))));
                c14.setCellStyle(excelStyle.getStyle("number"));

                Cell c15 = dataRow.createCell(15);
                c15.setCellValue(Double.parseDouble(String.valueOf(row.get("crd_fee_amt"))));
                c15.setCellStyle(excelStyle.getStyle("number"));

                Cell c16 = dataRow.createCell(16);
                c16.setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                c16.setCellStyle(excelStyle.getStyle("number"));
                
                dataRow.createCell(17).setCellValue(String.valueOf(row.get("wd_memo")));
            }
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 17);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "withdraw.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "wdMng/procRemitMain", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcRemitMain(@ModelAttribute("ProcRemitVO") @Valid ProcRemitVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return withdrawService.callProcRemitMain(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
        // return result;
    }

    @RequestMapping(value = "wdMng/changeWdStatus", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO changeWdStatus(@ModelAttribute("ProcRemitVO") @Valid ProcRemitVO procVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            // System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId()); 
            if (withdrawService.changeWdStatus(procVo)) {
                System.out.println("changeWdStatus Update  success");
                result.setResultCode("S000");
                result.setResultMsg("changeWdStatus Update successful.");
            } else {
                System.out.println("Chain Subtract Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("changeWdStatus Update Failed");
            }      
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the changeWdStatus.");
            e.printStackTrace();
            return result;
        }
        return result;
    }

    @RequestMapping(value = "wdMng/changeWorkDate", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO changeWorkDate(@ModelAttribute("ProcRemitVO") @Valid ProcRemitVO procVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId()); 
            if (withdrawService.changeWorkDate(procVo)) {
                System.out.println("changeWorkDate Update  success");
                result.setResultCode("S000");
                result.setResultMsg("change WorkDate Update successful.");
            } else {
                System.out.println("Chain Subtract Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("change WorkDate Update Failed");
            }      
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the change WorkDate.");
            e.printStackTrace();
            return result;
        }
        return result;
    }


    @RequestMapping("remitMng/remitSummary")    
    public @ResponseBody String getRemitSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            // System.out.println(hashmapParam);

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getRemitSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

            totalSumm = withdrawService.getRemitSummaryTotal(hashmapParam);    

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

    @RequestMapping("remitMng/remitList")    
    public @ResponseBody String getRemitList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                System.out.println("++++++++++++++++++++ empty");
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            
            list = withdrawService.getRemitList(hashmapParam);
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

    @RequestMapping("remitMng/remitSubRecvList")    
    public @ResponseBody String getRemitSubRecvList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();        
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null;         

        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam); 

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getRemitSubRecvList(hashmapParam);
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

    

    @RequestMapping(value = "remitMng/remitBankExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> remitBankExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            // hashmapParam.put("sidx", "");
            // hashmapParam.put("sord", "");
            // hashmapParam.put("start", "0");
            // hashmapParam.put("end", "9999");
            // List<HashMap<String, Object>> list = withdrawService.getRemitSummary(hashmapParam);
            List<HashMap<String, Object>> list = withdrawService.getRemitBankList(hashmapParam);
            

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Bank List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            // Row headerRow = sheet.createRow(0);
            // String[] headers = {
            //     // 은행명 계좌번호 송금액 예금주 빈칸 빈칸 가맹점명 운영사명
            //     "송금은행" , "계좌번호" , "송금액", "예금주", "", "",  "가맹점명"       , "운영사"
            // };
            // for (int i = 0; i < headers.length; i++) {
            //     Cell cell = headerRow.createCell(i);
            //     cell.setCellValue(headers[i]);
            // }
            // Populate data rows
            int rowIndex = 0;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);                
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("bbank_nm")));            // 은행명
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("bbank_account_no")));    // 계좌번호
                
                Cell c02 = dataRow.createCell(2);
                c02.setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                c02.setCellStyle(excelStyle.getStyle("number")); 
                
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("bbank_depositor")));              // 입금계좌 예금주
                dataRow.createCell(4).setCellValue("");                                                 // 빈칸
                dataRow.createCell(5).setCellValue("");                                                 // 빈칸
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("chain_nm")));            // 가맹점명  
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("op_nm")));               // 송금처:운영사명    
                if (rowIndex==1){
                    for (int i = 0; i < 7; i++) {
                        sheet.autoSizeColumn(i);
                        sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
                    }
                }
            }
            excelStyle.setRegionBorder(sheet, 0, rowIndex, 0, 7);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "remitList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "remitMng/remitSummExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> remitSummaryExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = withdrawService.getRemitSummary(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("가맹점 출금 정산 내역");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1); 
            String[] headers = {
                  "가맹점 명"       , "사업자 번호"         , "대표자명"          ,  "송금 상태"       
                , "송금 금액"       , "매출 금액"           , "카드사 수수료"       , "입금예정액"
                , "정산 원금"       , "서비스 수수료"       , "여신수수료"         , "정산 금액"
                , "과입금액"        , "차감 정산액"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(excelStyle.getStyle("header"));
                // 각 컬럼 너비 자동 조정
                sheet.autoSizeColumn(i);                
                // 한글의 경우 autoSizeColumn이 완벽하지 않을 수 있어 약간의 여백 추가
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }
            // Populate data rows
            int rowIndex = 2;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("chain_nm")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("biz_no")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("ceo_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("remit_status_nm")));

                Cell c04 = dataRow.createCell(4);
                c04.setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                c04.setCellStyle(excelStyle.getStyle("number"));
                
                Cell c05 = dataRow.createCell(5);
                c05.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                c05.setCellStyle(excelStyle.getStyle("number"));

                Cell c06 = dataRow.createCell(6);
                c06.setCellValue(Double.parseDouble(String.valueOf(row.get("card_fee_amt"))));
                c06.setCellStyle(excelStyle.getStyle("number"));

                Cell c07 = dataRow.createCell(7);
                c07.setCellValue(Double.parseDouble(String.valueOf(row.get("card_resv_amt"))));
                c07.setCellStyle(excelStyle.getStyle("number"));

                Cell c08 = dataRow.createCell(8);
                c08.setCellValue(Double.parseDouble(String.valueOf(row.get("wd_base_amt"))));
                c08.setCellStyle(excelStyle.getStyle("number"));

                Cell c09 = dataRow.createCell(9);
                c09.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_fee_amt"))));
                c09.setCellStyle(excelStyle.getStyle("number"));

                Cell c10 = dataRow.createCell(10);
                c10.setCellValue(Double.parseDouble(String.valueOf(row.get("crd_fee_amt"))));
                c10.setCellStyle(excelStyle.getStyle("number"));

                Cell c11 = dataRow.createCell(11);
                c11.setCellValue(Double.parseDouble(String.valueOf(row.get("wd_remit_amt"))));
                c11.setCellStyle(excelStyle.getStyle("number"));

                Cell c12 = dataRow.createCell(12);
                c12.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_remit_amt"))));
                c12.setCellStyle(excelStyle.getStyle("number"));

                Cell c13 = dataRow.createCell(13);
                c13.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_amt"))));
                c13.setCellStyle(excelStyle.getStyle("number")); 
            }

            // 3. 합계 Row 생성           

            Row sumRow = sheet.createRow(rowIndex);            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIndex, rowIndex, 0, 3));
            Cell summTitle = sumRow.createCell(0);
            summTitle.setCellValue("합계");            
            summTitle.setCellStyle(excelStyle.getStyle("header"));

            int[] sumCols = {4,5,6,7,8,9,10,11,12,13};
            for (int col : sumCols) {
                String colLetter = CellReference.convertNumToColString(col);
                String formula = String.format("SUM(%s2:%s%d)", colLetter, colLetter, rowIndex);
                Cell sumCell = sumRow.createCell(col);
                sumCell.setCellFormula(formula);
                sumCell.setCellStyle(excelStyle.getStyle("subTotal")); 
            }

            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 13);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "remitList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "remitMng/remitListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> remitListExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = withdrawService.getRemitList(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("출금 상세 리스트");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1); 
            String[] headers = {
                  "정산 번호"       , "정산 상태"       , "VAN"         , "매입사"          ,  "카드번호"       , "카드유형"
                , "구분"           , "승인번호"        , "승인일시"         , "입금예정일"       , "승인금액"
                , "카드수수료"      , "입금예정액"       , "서비스수수료"      , "정산 원금"       , "여신수수료"
                , "출금예정액"      , "비고"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(excelStyle.getStyle("header"));
                // 각 컬럼 너비 자동 조정
                sheet.autoSizeColumn(i);                
                // 한글의 경우 autoSizeColumn이 완벽하지 않을 수 있어 약간의 여백 추가
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }
            // Populate data rows
            int rowIndex = 2;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("wd_no")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("wd_status_nm")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("van_cd_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("card_acq_nm")));
                dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_no")));
                dataRow.createCell(5).setCellValue(String.valueOf(row.get("card_type_nm")));
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_gb_nm")));
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_no")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("conf_dttm")));
                dataRow.createCell(9).setCellValue(String.valueOf(row.get("card_resv_date")));

                Cell c9 = dataRow.createCell(10);
                c9.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                c9.setCellStyle(excelStyle.getStyle("number"));

                Cell c10 = dataRow.createCell(11);
                c10.setCellValue(Double.parseDouble(String.valueOf(row.get("card_fee_amt"))));
                c10.setCellStyle(excelStyle.getStyle("number"));

                Cell c11 = dataRow.createCell(12);
                c11.setCellValue(Double.parseDouble(String.valueOf(row.get("card_resv_amt"))));
                c11.setCellStyle(excelStyle.getStyle("number"));

                Cell c12 = dataRow.createCell(13);
                c12.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_fee_amt"))));
                c12.setCellStyle(excelStyle.getStyle("number"));

                Cell c13 = dataRow.createCell(14);
                c13.setCellValue(Double.parseDouble(String.valueOf(row.get("wd_base_amt"))));
                c13.setCellStyle(excelStyle.getStyle("number"));

                Cell c14 = dataRow.createCell(15);
                c14.setCellValue(Double.parseDouble(String.valueOf(row.get("crd_fee_amt"))));
                c14.setCellStyle(excelStyle.getStyle("number"));

                Cell c15 = dataRow.createCell(16);
                c15.setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                c15.setCellStyle(excelStyle.getStyle("number"));

                dataRow.createCell(17).setCellValue(String.valueOf(row.get("wd_memo")));
            }
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 17);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "remitList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @RequestMapping(value = "remitMng/remitCancel", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcRemitCancel(@ModelAttribute("ProcRemitVO") @Valid ProcRemitVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return withdrawService.callProcRemitCancel(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
        // return result;
    }

    @RequestMapping(value = "remitMng/remitSend", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcRemitSend(@ModelAttribute("ProcRemitVO") @Valid ProcRemitVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return withdrawService.callProcRemitSend(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
        // return result;
    }


    @RequestMapping("unRemit/urSummary")    
    public @ResponseBody String getURSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getURSummary(hashmapParam);
            int records = withdrawService.getQueryTotalCnt();

            totalSumm= withdrawService.getURSummaryTotal(hashmapParam);

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

    @RequestMapping("unRemit/urCardSummary")    
    public @ResponseBody String getURCardSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            if (pageing.getOrder() != null && !pageing.getOrder().isEmpty()) {
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getURCardSummary(hashmapParam);
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

    @RequestMapping("unRemit/unRemitList")    
    public @ResponseBody String getUnRemitList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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
                int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
                hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
                hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));                               
            } else {
                hashmapParam.put("sidx", pageing.getColumns().get(0).get("data"));
                hashmapParam.put("sord", "");                
            } 
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = withdrawService.getUnRemitList(hashmapParam);
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

    @RequestMapping(value = "unRemit/unRemitListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> unRemitListExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = withdrawService.getUnRemitList(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("정산 상세 리스트");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                  "정산 번호"       , "정산 상태"       , "매입사"          , "VAN"             ,  "카드번호"       , "카드유형"
                , "구분"           , "승인번호"        , "승인일시"         , "입금예정일"       , "승인금액"
                , "카드수수료"      , "입금예정액"       , "서비스수수료"     , "정산 원금"       , "여신수수료"
                , "출금예정액"      , "비고"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(excelStyle.getStyle("header"));
                // 각 컬럼 너비 자동 조정
                sheet.autoSizeColumn(i);                
                // 한글의 경우 autoSizeColumn이 완벽하지 않을 수 있어 약간의 여백 추가
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }
            // Populate data rows
            int rowIndex = 2;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("wd_no")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("wd_status_nm")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("card_acq_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("van_cd_nm")));
                dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_no")));
                dataRow.createCell(5).setCellValue(String.valueOf(row.get("card_type_nm")));
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_gb_nm")));
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_no")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("conf_dttm")));
                dataRow.createCell(9).setCellValue(String.valueOf(row.get("card_resv_date")));
                
                Cell c10 = dataRow.createCell(10);
                c10.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                c10.setCellStyle(excelStyle.getStyle("number"));
                
                Cell c11 = dataRow.createCell(11);
                c11.setCellValue(Double.parseDouble(String.valueOf(row.get("card_fee_amt"))));
                c11.setCellStyle(excelStyle.getStyle("number"));

                Cell c12 = dataRow.createCell(12);
                c12.setCellValue(Double.parseDouble(String.valueOf(row.get("card_resv_amt"))));
                c12.setCellStyle(excelStyle.getStyle("number"));

                Cell c13 = dataRow.createCell(13);
                c13.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_fee_amt"))));
                c13.setCellStyle(excelStyle.getStyle("number"));

                Cell c14 = dataRow.createCell(14);
                c14.setCellValue(Double.parseDouble(String.valueOf(row.get("wd_base_amt"))));
                c14.setCellStyle(excelStyle.getStyle("number"));

                Cell c15 = dataRow.createCell(15);
                c15.setCellValue(Double.parseDouble(String.valueOf(row.get("crd_fee_amt"))));
                c15.setCellStyle(excelStyle.getStyle("number"));

                Cell c16 = dataRow.createCell(16);
                c16.setCellValue(Double.parseDouble(String.valueOf(row.get("remit_amt"))));
                c16.setCellStyle(excelStyle.getStyle("number"));
                
                dataRow.createCell(17).setCellValue(String.valueOf(row.get("wd_memo")));
            }
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 17);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "withdraw.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}