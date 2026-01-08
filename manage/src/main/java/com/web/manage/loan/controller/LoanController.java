package com.web.manage.loan.controller;

import java.io.ByteArrayOutputStream;
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
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.web.manage.loan.domain.ProcSubTransVO;
import com.web.manage.loan.service.LoanService;
import com.web.common.util.ExcelStyleUtil;

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

    @RequestMapping(value = "loanMng/loanSummaryExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getLoanSummaryExcel(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        hashmapParam.put("userCorpCd", member.getUserCorpCd());
        hashmapParam.put("userCorpType", member.getUserCorpType());        
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = loanService.getLoanSummary(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("지원금 관리");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                "가 맹 점 명"        , "대 표 자"      , "즉 결 잔 고"     ,  "비즈론 대 출"       , "스팟자금 대출"
                , "기타 대출"        , "전체 대출잔액"  , "계약상태"        , "출금상태"
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
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("ceo_nm")));
                Cell c02 = dataRow.createCell(2);
                c02.setCellValue(Double.parseDouble(String.valueOf(row.get("tot_use_amt"))));
                c02.setCellStyle(excelStyle.getStyle("number"));
                
                Cell c03 = dataRow.createCell(3);
                c03.setCellValue(Double.parseDouble(String.valueOf(row.get("biz_loan_amt"))));
                c03.setCellStyle(excelStyle.getStyle("number"));

                Cell c04 = dataRow.createCell(4);
                c04.setCellValue(Double.parseDouble(String.valueOf(row.get("spot_loan_amt"))));
                c04.setCellStyle(excelStyle.getStyle("number"));
                
                Cell c05 = dataRow.createCell(5);
                c05.setCellValue(Double.parseDouble(String.valueOf(row.get("etc_loan_amt"))));
                c05.setCellStyle(excelStyle.getStyle("number"));

                Cell c06 = dataRow.createCell(6);
                c06.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_amt"))));
                c06.setCellStyle(excelStyle.getStyle("number"));

                dataRow.createCell(7).setCellValue(String.valueOf(row.get("svc_stat_nm")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("remit_stat_nm")));
 
            }
            // 테두리 그리기
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 8);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "chain_loan_list.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
 
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

    @RequestMapping("loanMng/loanRecvLog")    
    public @ResponseBody String getLoanRecvLogList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            list = loanService.getLoanRecvLogList(hashmapParam);
            int records = loanService.getQueryTotalCnt();
            totalSumm = loanService.getLoanRecvLogTotal(hashmapParam);

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

    @RequestMapping("loanMng/excelRepaySchdule")    
    public ResponseEntity<byte[]> getExcelRepaySchedule(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        // HashMap<String, Object> hashmapResult = new HashMap<String, Object>();		
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());        
        
        try {
            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 10));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("상환일정");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));
            
            Row headerRow = sheet.createRow(1);

            String[] headers = {
                "회차"          , "상환예정일"       , "거래전잔액"          ,  "청구원금"        , "청구이자"         , "총청구금액"
                , "미수원금"    , "미수이자"      , "미수총액"       , "상환일"       , "비고"
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

            // 모든 컬럼의 너비를 동일하게 설정하고 싶다면
            // for (int i = 0; i < headers.length; i++) {
            //     sheet.setColumnWidth(i, 5000); // 고정 너비 설정 (예: 5000)
            // }

			String loan_no = (String) hashmapParam.get("loan_no"); 
            System.out.println(loan_no);
            int rowIndex = 2;

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

                for (LoanRepayScheduleVO row : list) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(String.valueOf(row.getSc_seq()));
                    dataRow.createCell(1).setCellValue(String.valueOf(row.getSc_date()));

                    Cell c2 = dataRow.createCell(2);
                    c2.setCellValue(Double.parseDouble(String.valueOf(row.getBalance_amt())));
                    c2.setCellStyle(excelStyle.getStyle("number"));

                    Cell c3 = dataRow.createCell(3);
                    c3.setCellValue(Double.parseDouble(String.valueOf(row.getRepay_princ_amt())));
                    c3.setCellStyle(excelStyle.getStyle("number"));

                    Cell c4 = dataRow.createCell(4);
                    c4.setCellValue(Double.parseDouble(String.valueOf(row.getRepay_int_amt())));
                    c4.setCellStyle(excelStyle.getStyle("number"));

                    Cell c5 = dataRow.createCell(5);
                    c5.setCellValue(Double.parseDouble(String.valueOf(row.getRepay_tot_amt())));
                    c5.setCellStyle(excelStyle.getStyle("number"));

                    Cell c6 = dataRow.createCell(6);
                    c6.setCellValue(Double.parseDouble(String.valueOf(row.getRemain_princ_amt())));
                    c6.setCellStyle(excelStyle.getStyle("number"));

                    Cell c7 = dataRow.createCell(7);
                    c7.setCellValue(Double.parseDouble(String.valueOf(row.getRemain_int_amt())));
                    c7.setCellStyle(excelStyle.getStyle("number"));

                    Cell c8 = dataRow.createCell(8);
                    c8.setCellValue(Double.parseDouble(String.valueOf(row.getRemain_tot_amt())));
                    c8.setCellStyle(excelStyle.getStyle("number")); 
                    dataRow.createCell(9).setCellValue(String.valueOf(row.getRecv_dt()));
                    dataRow.createCell(10).setCellValue(String.valueOf(row.getRecv_yn_nm()));  
                }
			} else {
                List<HashMap<String, Object>> list  = new ArrayList<HashMap<String, Object>>();
                list = loanService.getLoanRepaymentList(hashmapParam); 
                
                // Populate data rows 
                for (HashMap<String, Object> row : list) {
                    
                    Row dataRow = sheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(String.valueOf(row.get("sc_seq")));
                    dataRow.createCell(1).setCellValue(String.valueOf(row.get("sc_date"))); 

                    Cell c2 = dataRow.createCell(2);
                    c2.setCellValue(Double.parseDouble(String.valueOf(row.get("balance_amt"))));
                    c2.setCellStyle(excelStyle.getStyle("number"));

                    Cell c3 = dataRow.createCell(3);
                    c3.setCellValue(Double.parseDouble(String.valueOf(row.get("repay_princ_amt"))));
                    c3.setCellStyle(excelStyle.getStyle("number"));

                    Cell c4 = dataRow.createCell(4);
                    c4.setCellValue(Double.parseDouble(String.valueOf(row.get("repay_int_amt"))));
                    c4.setCellStyle(excelStyle.getStyle("number"));

                    Cell c5 = dataRow.createCell(5);
                    c5.setCellValue(Double.parseDouble(String.valueOf(row.get("repay_tot_amt"))));
                    c5.setCellStyle(excelStyle.getStyle("number"));

                    Cell c6 = dataRow.createCell(6);
                    c6.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_princ_amt"))));
                    c6.setCellStyle(excelStyle.getStyle("number"));

                    Cell c7 = dataRow.createCell(7);
                    c7.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_int_amt"))));
                    c7.setCellStyle(excelStyle.getStyle("number"));

                    Cell c8 = dataRow.createCell(8);
                    c8.setCellValue(Double.parseDouble(String.valueOf(row.get("remain_tot_amt"))));
                    c8.setCellStyle(excelStyle.getStyle("number")); 
                    dataRow.createCell(9).setCellValue(String.valueOf(row.get("recv_dt")));
                    dataRow.createCell(10).setCellValue(String.valueOf(row.get("recv_yn_nm"))); 
                }
			}
            // 3. 합계 Row 생성
            System.out.println(rowIndex);

            Row sumRow = sheet.createRow(rowIndex);            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIndex, rowIndex, 0, 2));
            Cell summTitle = sumRow.createCell(0);
            summTitle.setCellValue("합계");            
            summTitle.setCellStyle(excelStyle.getStyle("header"));

            int[] sumCols = {3,4,5,6,7,8};
            for (int col : sumCols) {
                String colLetter = CellReference.convertNumToColString(col);
                String formula = String.format("SUM(%s2:%s%d)", colLetter, colLetter, rowIndex);
                Cell sumCell = sumRow.createCell(col);
                sumCell.setCellFormula(formula);
                sumCell.setCellStyle(excelStyle.getStyle("subTotal")); 
            }
            excelStyle.setRegionBorder(sheet, 2, rowIndex, 0, 10);

            // Write workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Set response headers
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hHeaders.setContentDispositionFormData("attachment", "schedule.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());             
			
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

	@RequestMapping(value = "loanMng/insertLoanMst", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertLoanMst(@ModelAttribute("LoanMstVO") @Valid LoanMstVO loanMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
		try {
			SessionVO member = (SessionVO) session.getAttribute("S_USER");
			loanMstVo.setEnt_user_id(member.getUserId());
            loanMstVo.setLoan_status("01");             //진행중
			loanMstVo.setPrinc_amt(loanMstVo.getPrinc_amt().replace(",", ""));
            loanMstVo.setInt_amt(loanMstVo.getInt_amt().replace(",", ""));
            loanMstVo.setTot_loan_amt(loanMstVo.getTot_loan_amt().replace(",", ""));
			loanMstVo.setLoan_no(commonService.getJobSeq("TB_LOAN_MST", "LOAN_NO"));

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

    @RequestMapping(value = "loanMng/sendSubTrans", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO sendSubTrans(@ModelAttribute("ProcSubTransVO") @Valid ProcSubTransVO procVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();  
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUser_id(member.getUserId());            
            return loanService.callProcLoanSendSubTrans(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the Loan Prepay. [procLoanPrepay]");
            e.printStackTrace();
            return result;
        }
    }

    @RequestMapping(value = "loanMng/procLoanPrepay", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcLoanPrepay(@ModelAttribute("ProcPrepayVO") @Valid ProcPrepayVO procVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();  
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setPrepay_user_id(member.getUserId());            
            return loanService.callProcLoanPrepay(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the Loan Prepay. [procLoanPrepay]");
            e.printStackTrace();
            return result;
        }  
    }
 

    @RequestMapping(value = "loanMng/deleteLoanMst", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO deleteLoanMst(@ModelAttribute("LoanMstVO") @Valid LoanMstVO loanMstVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            loanMstVo.setUpt_user_id(member.getUserId());          
            return loanService.deleteLoanMst(loanMstVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the Delete Loan. [deleteLoanMst]");
            e.printStackTrace();
            return result;
        } 
    }
	

}
