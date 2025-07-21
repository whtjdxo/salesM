package com.web.manage.statis.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.common.util.ExcelStyleUtil;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.statis.service.SalesReportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/statis/report/")
public class SalesReportController {
    @Autowired
    private SalesReportService salesReportService;

    @RequestMapping("salesReport/view")
    public String view() {
        return "pages/statis/salesReport";
    }

    @RequestMapping("salesTransition/view")
    public String salesTransitionview() {
        return "pages/statis/salesTransition";
    }

    @RequestMapping(value = "salesReport/salesSummary", method = RequestMethod.POST)
    public @ResponseBody String getSalesSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType());
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
            
            list = salesReportService.getSalesSummary(hashmapParam);
            int records = salesReportService.getQueryTotalCnt();
            totalSumm = salesReportService.getSalesSummaryTotal(hashmapParam);

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

    @RequestMapping(value = "salesReport/downExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getSalesSummaryToExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = salesReportService.getSalesSummary(hashmapParam);

            String closeChain= hashmapParam.get("sch_chain_no") != null ? hashmapParam.get("sch_chain_no").toString() : "";

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");

            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("매출현황");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "NO", "정산 일자"
                // 출금마감 - 매출
                , "출금-총승인금액", "출금 원금","출금-운영사 수수료", "출금-여신사 수수료", "출금-소계"
                , "입금-총승인금액", "입금 원금","입금-운영사 수수료", "입금-여신사 수수료", "입금-소계"
                , "즉결잔고"
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
                int colIndex = 0;
                // 1. NO (row number)
                dataRow.createCell(colIndex++).setCellValue(rowIndex - 1);                
                // 2. 출금
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("close_date", "").toString());

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number")); 

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sales_total"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sales_total"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("db_bank_in_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("db_bank_in_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_sales_total"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_sales_total"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 12. 총 사용액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                
            }           
            
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 12);
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


    @RequestMapping(value = "salesReport/salesTransition", method = RequestMethod.POST)
    public @ResponseBody String getSalesTransition(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType());
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
            
            list = salesReportService.getSalesTransition(hashmapParam);
            int records = salesReportService.getQueryTotalCnt();
            totalSumm = salesReportService.getSalesTransitionTotal(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("totalSumm", totalSumm);

            jString = gson.toJson(hashmapResult);
            System.err.println(jString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }    
    
    @RequestMapping(value="salesReport/chainSalesTransition", method = RequestMethod.POST)
	public @ResponseBody ReturnDataVO chainSalesTransition(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session){
		HashMap<String, Object> chainSalesInfo = new HashMap<String, Object>();
		ReturnDataVO result = new ReturnDataVO();
		try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            hashmapParam.put("user_id", member.getUserId());
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
            hashmapParam.put("userCorpType", member.getUserCorpType());
			chainSalesInfo = salesReportService.getChainSalesTransition(hashmapParam); 
			result.setResultCode("S000");
			result.setData(chainSalesInfo);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}
}
