package com.web.manage.billing.controller;

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
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.SessionVO;
import com.web.common.util.ExcelStyleUtil;
import com.web.manage.billing.service.AgencyFeeService; 

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/statis/billing/")
public class AgencyFeeController {
    @Autowired
    private AgencyFeeService agencyFeeService;

    @RequestMapping("agencyFee/view")
    public String view() {
        return "pages/billing/agencyFee";
    }

    @RequestMapping(value = "agencyFee/feeSummary", method = RequestMethod.POST)
    public @ResponseBody String getAgencyFeeSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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
            
            list = agencyFeeService.getAgencyFeeSummary(hashmapParam);
            int records = agencyFeeService.getQueryTotalCnt();
            totalSumm = agencyFeeService.getAgencyFeeSummaryTotal(hashmapParam);

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

    @RequestMapping(value = "agencyFee/chainFeeList", method = RequestMethod.POST)
    public @ResponseBody String getAgencyFeeList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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
            
            list = agencyFeeService.getAgencyFeeList(hashmapParam);
            int records = agencyFeeService.getQueryTotalCnt();
            totalSumm = agencyFeeService.getAgencyFeeListTotal(hashmapParam);

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

    @RequestMapping(value = "agencyFee/downExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getAgncyFeeSummaryToExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = agencyFeeService.getAgencyFeeSummary(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("수수료 현황");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "NO", "대리점 명", "가맹점 명", "대표자 명"                
                , "승인건수", "승인금액","정산 원금액", "정산 수수료", "여신수수료", "비즈론수수료"
                , "대리점-정산수수료", "대리점-여신수수료", "대리점-비즈론수수료", "대리점-지급총액"
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
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("agency_nm", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("chain_nm", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("ceo_nm", "").toString());

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("conf_cnt")))); 
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_cnt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_svc_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_svc_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_crd_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_crd_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_loan_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_loan_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_tot_fee"))));                  
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_tot_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
            }    
            Row sumRow = sheet.createRow(rowIndex);            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIndex, rowIndex, 0, 3));
            Cell summTitle = sumRow.createCell(0);
            summTitle.setCellValue("합계");            
            summTitle.setCellStyle(excelStyle.getStyle("header"));

            int[] sumCols = {4,5,6,7,8,9,10,11,12};
            for (int col : sumCols) {
                String colLetter = CellReference.convertNumToColString(col);
                String formula = String.format("SUM(%s2:%s%d)", colLetter, colLetter, rowIndex);
                Cell sumCell = sumRow.createCell(col);
                sumCell.setCellFormula(formula);
                sumCell.setCellStyle(excelStyle.getStyle("subTotal")); 
            }
            
            // 테두리 그리기
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

    @RequestMapping(value = "agencyFee/downListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getAgncyFeeListToExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = agencyFeeService.getAgencyFeeList(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("수수료 현황");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "NO", "정산일"
                , "승인건수", "승인금액","정산 원금액", "정산 수수료", "여신수수료", "비즈론수수료"
                , "대리점-정산수수료", "대리점-여신수수료", "대리점-비즈론수수료", "대리점-지급총액"
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
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("adjust_date", "").toString());

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("conf_cnt")))); 
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_cnt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("bank_in_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 

                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_svc_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_svc_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_crd_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_crd_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_loan_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_loan_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("agent_tot_fee"))));                 
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("agent_tot_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));                 
            }    
            Row sumRow = sheet.createRow(rowIndex);            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIndex, rowIndex, 0, 1));
            Cell summTitle = sumRow.createCell(0);
            summTitle.setCellValue("합계");            
            summTitle.setCellStyle(excelStyle.getStyle("header"));

            int[] sumCols = {2,3,4,5,6,7,8,9,10,11};
            for (int col : sumCols) {
                String colLetter = CellReference.convertNumToColString(col);
                String formula = String.format("SUM(%s2:%s%d)", colLetter, colLetter, rowIndex);
                Cell sumCell = sumRow.createCell(col);
                sumCell.setCellFormula(formula);
                sumCell.setCellStyle(excelStyle.getStyle("subTotal")); 
            }
            
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 11);
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
 
}
