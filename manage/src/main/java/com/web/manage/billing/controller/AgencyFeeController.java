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

            int[] sumCols = {4,5,6,7,8,9,10,11,12,13};
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
            hHeaders.setContentDispositionFormData("attachment", "agencyFee.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    } 

    @RequestMapping(value = "agencyFee/downTaxExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getChainTaxExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = agencyFeeService.getChainTaxExcel(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("TAX List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("계산서 발행 내역");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(6);
            String[] headers = {
                "전자(세금)계산서 종류(01:일반, 02:영세율)", "작성일자", "공급자 등록번호('-'없이 입력)", "공급자 종사업장번호", "공급자 상호",
                "공급자 성명", "공급자 사업장주소", "공급자 업태", "공급자 종목", "공급자 이메일",
                "공급받는자 등록번호('-'없이 입력)", "공급받는자 종사업장번호", "공급받는자 상호", "공급받는자 성명", "공급받는자 사업장주소",
                "공급받는자 업태", "공급받는자 종목", "공급받는자 이메일1", "공급받는자 이메일2", "공급가액","세액", "비고", "일자1(2자리, 작성년월 제외)",
                "품목1", "규격1", "수량1", "단가1", "공급가액1", "세액1", "품목비고1", "일자2(2자리, 작성년월 제외)",
                "품목2", "규격2", "수량2", "단가2", "공급가액2", "세액2", "품목비고2", "일자3(2자리, 작성년월 제외)",
                "품목3", "규격3", "수량3", "단가3", "공급가액3", "세액3", "품목비고3", "일자4(2자리, 작성년월 제외)",
                "품목4", "규격4", "수량4", "단가4", "공급가액4", "세액4", "품목비고4",
                "현금", "수표", "어음", "외상미수금", "영수(01),청구(02)"
            };
            // 셀에 줄바꿈 스타일 적용
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(excelStyle.getStyle("header"));
                cell.getCellStyle().setWrapText(true); // 줄바꿈 적용
                // 각 컬럼 너비 자동 조정
                sheet.autoSizeColumn(i);
                // 한글의 경우 autoSizeColumn이 완벽하지 않을 수 있어 약간의 여백 추가
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }            
             
            // Populate data rows
            int rowIndex = 7;
            for (HashMap<String, Object> row : list) {
                Row dataRow = sheet.createRow(rowIndex++);
                int colIndex = 0;
                // System.out.println("------------------------->> 공급자 정보 - 운영사 정보");
                dataRow.createCell(colIndex++).setCellValue("01");                                
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("tax_dt", "").toString());
                dataRow.createCell(colIndex++).setCellValue("1618100518");
                dataRow.createCell(colIndex++).setCellValue("");
                dataRow.createCell(colIndex++).setCellValue("주식회사 마트페이");
                dataRow.createCell(colIndex++).setCellValue("권해진");
                dataRow.createCell(colIndex++).setCellValue("경기도 시흥시 거북섬공원로 27, 6층 651호(정왕동)");
                dataRow.createCell(colIndex++).setCellValue("서비스");
                dataRow.createCell(colIndex++).setCellValue("금융지원서비스업");
                dataRow.createCell(colIndex++).setCellValue("geetae@naver.com"); 
                // System.out.println("------------------------->> 공급받는자 정보 - 가맹점 정보");
                // 공급받는자 정보 - 가맹점 정보                
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("biz_no", "").toString().replace("-", ""));
                dataRow.createCell(colIndex++).setCellValue("");
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("chain_nm", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("boss_nm", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("chain_addr", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("uptae", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("upjong", "").toString());
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("email", "").toString());
                dataRow.createCell(colIndex++).setCellValue("");                                                //email2
                
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_sales_amt"))));                // 공급가액
                cell.setCellStyle(excelStyle.getStyle("number"));                
                
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_vat_amt"))));                  // 세액
                cell.setCellStyle(excelStyle.getStyle("number"));                         
                
                dataRow.createCell(colIndex++).setCellValue("");                                                    // 비고                 
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("tax_dt", "").toString().substring(8, 10));     
                dataRow.createCell(colIndex++).setCellValue("정산수수료");             // 품목1
                dataRow.createCell(colIndex++).setCellValue("");	            // 규격1
                dataRow.createCell(colIndex++).setCellValue("");	            // 수량1
                dataRow.createCell(colIndex++).setCellValue("");	            // 단가1

                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_sales_amt"))));                // 공급가액1
                cell.setCellStyle(excelStyle.getStyle("number"));                

                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("svc_vat_amt"))));                  // 세액1
                cell.setCellStyle(excelStyle.getStyle("number"));                         
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목비고1

                dataRow.createCell(colIndex++).setCellValue("");	            // 일자2(2자리, 작성년월 제외)
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목2
                dataRow.createCell(colIndex++).setCellValue("");	            // 규격2
                dataRow.createCell(colIndex++).setCellValue("");	            // 수량2
                dataRow.createCell(colIndex++).setCellValue("");	            // 단가2
                dataRow.createCell(colIndex++).setCellValue("");	            // 공급가액2
                dataRow.createCell(colIndex++).setCellValue("");	            // 세액2
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목비고2
                dataRow.createCell(colIndex++).setCellValue("");	            // 일자3(2자리, 작성년월 제외)
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목3
                dataRow.createCell(colIndex++).setCellValue("");	            // 규격3
                dataRow.createCell(colIndex++).setCellValue("");	            // 수량3
                dataRow.createCell(colIndex++).setCellValue("");	            // 단가3
                dataRow.createCell(colIndex++).setCellValue("");	            // 공급가액3
                dataRow.createCell(colIndex++).setCellValue("");	            // 세액3
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목비고3
                dataRow.createCell(colIndex++).setCellValue("");	            // 일자4(2자리, 작성년월 제외)
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목4
                dataRow.createCell(colIndex++).setCellValue("");	            // 규격4
                dataRow.createCell(colIndex++).setCellValue("");	            // 수량4
                dataRow.createCell(colIndex++).setCellValue("");	            // 단가4
                dataRow.createCell(colIndex++).setCellValue("");	            // 공급가액4
                dataRow.createCell(colIndex++).setCellValue("");	            // 세액4
                dataRow.createCell(colIndex++).setCellValue("");	            // 품목비고4
                dataRow.createCell(colIndex++).setCellValue("");	            // 현금
                dataRow.createCell(colIndex++).setCellValue("");	            // 수표
                dataRow.createCell(colIndex++).setCellValue("");	            // 어음
                dataRow.createCell(colIndex++).setCellValue("");	            // 외상미수금

                dataRow.createCell(colIndex++).setCellValue("01");	            // 영수(01),청구(02)                     
            }    

            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 58);
            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "chainTax.xlsx");

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
            System.out.println("=====list size=====>" + list.size());
            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("수수료 상세내역");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            String chainNm = "";
            if (hashmapParam.containsKey("sch_chain_nm")) {
                chainNm = String.valueOf(hashmapParam.get("sch_chain_nm"));
            }
            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(chainNm + " 수수료 상세내역");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "NO", "입금확인일"
                , "승인건수", "승인금액","정산 원금액", "정산 수수료", "여신수수료", "비즈론수수료"
                , "대리점-정산수수료", "대리점-여신수수료", "대리점-비즈론수수료", "대리점-지급총액"
            };
            
            System.out.println("=====headers length=====>" + headers.length);

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
                // 2. 입금확인일
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
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIndex, rowIndex, 0, 3));
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
            System.out.println("=====Excel Created=====>");
            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "agencyFeeList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
 
}
