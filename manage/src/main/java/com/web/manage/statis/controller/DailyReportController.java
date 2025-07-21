package com.web.manage.statis.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.web.manage.common.domain.SessionVO;
import com.web.manage.statis.service.DailyReportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/statis/report/")
public class DailyReportController {
    @Autowired
    private DailyReportService dailyReportService;

    @RequestMapping("dayReport/view")
    public String view() {
        return "pages/statis/dailyReport";
    }

    @RequestMapping(value = "dayReport/dailySummary", method = RequestMethod.POST)
    public @ResponseBody String getDailySummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");        
        hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
		hashmapParam.put("userCorpType", member.getUserCorpType());
		// System.out.println(hashmapParam.get("userCorpCd"));
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
            
            list = dailyReportService.getDailySummary(hashmapParam);
            int records = dailyReportService.getQueryTotalCnt();
            totalSumm = dailyReportService.getDailySummaryTotal(hashmapParam);

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

    @RequestMapping(value = "dayReport/dailyList", method = RequestMethod.POST)
    public @ResponseBody String getDailyListSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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
            
            list = dailyReportService.getDailyList(hashmapParam);
            int records = dailyReportService.getQueryTotalCnt();
            totalSumm = dailyReportService.getDailyListTotal(hashmapParam);

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

    @RequestMapping(value = "dayReport/downExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getDailySummaryToExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = dailyReportService.getDailySummary(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");

            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("영업일보");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1); 

            String[] headers = {
                "NO", "정산 일자",
                // 출금마감 - 매출
                "출금마감-매출-총건", "출금마감-매출-매출총액",
                // 출금마감 - 수수료
                "출금마감-수수료-카드사", "출금마감-수수료-운영사", "출금마감-수수료-여신사",
                // 출금마감 - 출금
                "출금마감-출금-원금", "출금마감-출금-차감액", "출금마감-출금-과입금", "출금마감-출금-총송금액",
                // 입금마감 - 매출금액
                "입금마감-매출금액",
                // 입금마감 - 입금예정액
                "입금마감-입금예정액-원금", "입금마감-입금예정액-운영수수료", "입금마감-입금예정액-여신수수료",
                // 입금마감 - 운영사 입금정산
                "입금마감-운영사입금정산-가맹점입금", "입금마감-운영사입금정산-운영사입금", "입금마감-운영사입금정산-원금",
                "입금마감-운영사입금정산-운영수수료", "입금마감-운영사입금정산-여신수수료", "입금마감-운영사입금정산-차액",
                "입금마감-운영사입금정산-입금수수료", "입금마감-운영사입금정산-과입처리", "입금마감-운영사입금정산-차감처리",
                // 입금마감 - 과입등록
                "입금마감-과입등록-과입금", "입금마감-과입등록-카드수수료", "입금마감-과입등록-이자오차", "입금마감-과입등록-운영과송금",
                // 입금마감 - 차감등록
                "입금마감-차감등록-원금", "입금마감-차감등록-이자", "입금마감-차감등록-카드수수료", "입금마감-차감등록-이자오차",
                "입금마감-차감등록-미집금원금", "입금마감-차감등록-미집금이자",
                // 입금마감 - 차감실행
                "입금마감-차감실행-원금", "입금마감-차감실행-카드수수료", "입금마감-차감실행-여신수수료",
                // 입금마감 - 수기차감
                "입금마감-수기차감-원금", "입금마감-수기차감-카드수수료", "입금마감-수기차감-여신수수료",
                // 여신 - 사업자대출 차감실행
                "여신-사업자대출-차감실행-대출원금", "여신-사업자대출-차감실행-여신수수료", "여신-사업자대출-차감실행-연체이자",
                // 여신 - 사업자대출 수기실행
                "여신-사업자대출-수기실행-대출원금", "여신-사업자대출-수기실행-여신수수료", "여신-사업자대출-수기실행-연체이자",
                // 여신 - 스팟 차감실행
                "여신-스팟-차감실행-대출원금", "여신-스팟-차감실행-여신수수료", "여신-스팟-차감실행-연체이자",
                // 여신 - 스팟 수기실행
                "여신-스팟-수기실행-대출원금", "여신-스팟-수기실행-여신수수료", "여신-스팟-수기실행-연체이자",
                // 기타
                "기타",
                // 총 사용금액
                "현재", "전일", "증감"
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
                // 2. 마감-정산일자
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("close_date", "").toString());
                // 3. 출금마감-매출-총건
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_cnt")))); 
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_cnt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 4. 출금마감-매출-매출총액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 5. 출금마감-수수료-카드사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_card_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_card_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 6. 출금마감-수수료-운영사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 7. 출금마감-수수료-여신사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 8. 출금마감-출금-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 9. 출금마감-출금-차감액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sub_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sub_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 10. 출금마감-출금-과입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_exc_remit_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_exc_remit_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 11. 출금마감-출금-총송금액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_tot_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_tot_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 12. 입금마감-매출금액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt_sum"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt_sum"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 13. 입금마감-입금예정액-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 14. 입금마감-입금예정액-운영수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 15. 입금마감-입금예정액-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 16. 입금마감-운영사입금정산-가맹점입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_bank_in_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_bank_in_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 17. 입금마감-운영사입금정산-운영사입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 18. 입금마감-운영사입금정산-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 19. 입금마감-운영사입금정산-운영수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 20. 입금마감-운영사입금정산-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 21. 입금마감-운영사입금정산-차액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 22. 입금마감-운영사입금정산-입금수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_remit_trans_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_remit_trans_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 23. 입금마감-운영사입금정산-과입처리
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_exc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_exc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 24. 입금마감-운영사입금정산-차감처리
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_sub_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_sub_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 25. 입금마감-과입등록-과입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_over_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_over_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 26. 입금마감-과입등록-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 27. 입금마감-과입등록-이자오차
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_date_change_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_date_change_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 28. 입금마감-과입등록-운영과송금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_crd_over_send_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 29. 입금마감-차감등록-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 30. 입금마감-차감등록-이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 31. 입금마감-차감등록-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 32. 입금마감-차감등록-이자오차
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_date_change_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_date_change_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 33. 입금마감-차감등록-미집금원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 34. 입금마감-차감등록-미집금이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 35. 입금마감-차감실행-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 36. 입금마감-차감실행-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 37. 입금마감-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 38. 입금마감-수기차감-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 39. 입금마감-수기차감-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 40. 입금마감-수기차감-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 41. 여신-사업자대출-차감실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 42. 여신-사업자대출-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 43. 여신-사업자대출-차감실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 44. 여신-사업자대출-수기실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 45. 여신-사업자대출-수기실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 46. 여신-사업자대출-수기실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 47. 여신-스팟-차감실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 48. 여신-스팟-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 49. 여신-스팟-차감실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 50. 여신-스팟-수기실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 51. 여신-스팟-수기실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 52. 여신-스팟-수기실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 53. 기타
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_loan_etc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_loan_etc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 54. 현재
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 55. 전일
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("pre_tot_use_amt"))));                
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("pre_tot_use_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 56. 증감
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt_gap"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt_gap"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
            }    
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 55); 
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

    @RequestMapping(value = "dayReport/downListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getDailyListToExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        try {
            hashmapParam.put("userCorpCd", member.getUserCorpCd());
		    hashmapParam.put("userCorpType", member.getUserCorpType());
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = dailyReportService.getDailyList(hashmapParam);
            
            String closeDate = hashmapParam.get("sch_close_date") != null ? hashmapParam.get("sch_close_date").toString() : "";

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");

            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("영업일보 (" + closeDate + ")");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "NO", "가맹점명",
                // 출금마감 - 매출
                "출금마감-매출-총건", "출금마감-매출-매출총액",
                // 출금마감 - 수수료
                "출금마감-수수료-카드사", "출금마감-수수료-운영사", "출금마감-수수료-여신사",
                // 출금마감 - 출금
                "출금마감-출금-원금", "출금마감-출금-차감액", "출금마감-출금-과입금", "출금마감-출금-총송금액",
                // 입금마감 - 매출금액
                "입금마감-매출금액",
                // 입금마감 - 입금예정액
                "입금마감-입금예정액-원금", "입금마감-입금예정액-운영수수료", "입금마감-입금예정액-여신수수료",
                // 입금마감 - 운영사 입금정산
                "입금마감-운영사입금정산-가맹점입금", "입금마감-운영사입금정산-운영사입금", "입금마감-운영사입금정산-원금",
                "입금마감-운영사입금정산-운영수수료", "입금마감-운영사입금정산-여신수수료", "입금마감-운영사입금정산-차액",
                "입금마감-운영사입금정산-입금수수료", "입금마감-운영사입금정산-과입처리", "입금마감-운영사입금정산-차감처리",
                // 입금마감 - 과입등록
                "입금마감-과입등록-과입금", "입금마감-과입등록-카드수수료", "입금마감-과입등록-이자오차", "입금마감-과입등록-운영과송금",
                // 입금마감 - 차감등록
                "입금마감-차감등록-원금", "입금마감-차감등록-이자", "입금마감-차감등록-카드수수료", "입금마감-차감등록-이자오차",
                "입금마감-차감등록-미집금원금", "입금마감-차감등록-미집금이자",
                // 입금마감 - 차감실행
                "입금마감-차감실행-원금", "입금마감-차감실행-카드수수료", "입금마감-차감실행-여신수수료",
                // 입금마감 - 수기차감
                "입금마감-수기차감-원금", "입금마감-수기차감-카드수수료", "입금마감-수기차감-여신수수료",
                // 여신 - 사업자대출 차감실행
                "여신-사업자대출-차감실행-대출원금", "여신-사업자대출-차감실행-여신수수료", "여신-사업자대출-차감실행-연체이자",
                // 여신 - 사업자대출 수기실행
                "여신-사업자대출-수기실행-대출원금", "여신-사업자대출-수기실행-여신수수료", "여신-사업자대출-수기실행-연체이자",
                // 여신 - 스팟 차감실행
                "여신-스팟-차감실행-대출원금", "여신-스팟-차감실행-여신수수료", "여신-스팟-차감실행-연체이자",
                // 여신 - 스팟 수기실행
                "여신-스팟-수기실행-대출원금", "여신-스팟-수기실행-여신수수료", "여신-스팟-수기실행-연체이자",
                // 기타
                "기타",
                // 총 사용금액
                "현재", "전일", "증감"
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
                // 2. 마감-정산일자
                dataRow.createCell(colIndex++).setCellValue(row.getOrDefault("chain_nm", "").toString());
                // 3. 출금마감-매출-총건
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_cnt")))); 
                Cell cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_cnt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 4. 출금마감-매출-매출총액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 5. 출금마감-수수료-카드사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_card_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_card_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 6. 출금마감-수수료-운영사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_svc_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));

                // 7. 출금마감-수수료-여신사
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_crd_fee_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 8. 출금마감-출금-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_wd_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 9. 출금마감-출금-차감액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sub_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_sub_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 10. 출금마감-출금-과입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_exc_remit_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_exc_remit_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 11. 출금마감-출금-총송금액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_tot_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("rm_remit_tot_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 12. 입금마감-매출금액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt_sum"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_conf_amt_sum"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 13. 입금마감-입금예정액-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 14. 입금마감-입금예정액-운영수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 15. 입금마감-입금예정액-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_resv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 16. 입금마감-운영사입금정산-가맹점입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("dp_bank_in_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("dp_bank_in_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 17. 입금마감-운영사입금정산-운영사입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 18. 입금마감-운영사입금정산-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 19. 입금마감-운영사입금정산-운영수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 20. 입금마감-운영사입금정산-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_bank_in_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 21. 입금마감-운영사입금정산-차액
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 22. 입금마감-운영사입금정산-입금수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_remit_trans_fee"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_remit_trans_fee"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 23. 입금마감-운영사입금정산-과입처리
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_exc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_exc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 24. 입금마감-운영사입금정산-차감처리
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_sub_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("op_send_sub_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 25. 입금마감-과입등록-과입금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_over_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_over_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 26. 입금마감-과입등록-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_card_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 27. 입금마감-과입등록-이자오차
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_date_change_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("exc_date_change_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 28. 입금마감-과입등록-운영과송금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("exc_crd_over_send_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 29. 입금마감-차감등록-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 30. 입금마감-차감등록-이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_unrecv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 31. 입금마감-차감등록-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_gap_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_card_gap_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 32. 입금마감-차감등록-이자오차
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_date_change_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_date_change_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 33. 입금마감-차감등록-미집금원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 34. 입금마감-차감등록-미집금이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("sub_svc_unsend_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 35. 입금마감-차감실행-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 36. 입금마감-차감실행-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 37. 입금마감-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 38. 입금마감-수기차감-원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 39. 입금마감-수기차감-카드수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 40. 입금마감-수기차감-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_svc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_svc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 41. 여신-사업자대출-차감실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 42. 여신-사업자대출-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 43. 여신-사업자대출-차감실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_biz_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 44. 여신-사업자대출-수기실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 45. 여신-사업자대출-수기실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 46. 여신-사업자대출-수기실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_biz_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 47. 여신-스팟-차감실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 48. 여신-스팟-차감실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 49. 여신-스팟-차감실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_spot_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 50. 여신-스팟-수기실행-대출원금
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_base_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_base_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 51. 여신-스팟-수기실행-여신수수료
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 52. 여신-스팟-수기실행-연체이자
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_delay_crd_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("mrecv_spot_delay_crd_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 53. 기타
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("recv_loan_etc_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("recv_loan_etc_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 54. 현재
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 55. 전일
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("pre_tot_use_amt"))));                
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("pre_tot_use_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                // 56. 증감
                // dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt_gap"))));
                cell = dataRow.createCell(colIndex++);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt_gap"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
            }    
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 55);
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
