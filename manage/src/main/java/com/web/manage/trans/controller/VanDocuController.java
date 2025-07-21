package com.web.manage.trans.controller;

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

import com.web.manage.trans.service.VanDocuService;

import ch.qos.logback.classic.Logger;

import com.web.manage.base.domain.ChainVanVO;
import com.web.manage.base.domain.CorpVO;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.web.manage.trans.domain.MapCodeVO;
import com.web.manage.trans.domain.TransProcessVO;
import com.web.manage.trans.domain.VanDocuVO;
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
@RequestMapping("/trans/trans/vanDocuMng/")
public class VanDocuController {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private VanDocuService vanDocuService;    

   @Autowired
   private CommonService commonService; 

    @RequestMapping("view")
    public String view() {
        return "pages/trans/vanDocuMng";
    }

    @RequestMapping("excel")
    public String excelUpload() {
        return "pages/trans/vanDocuUpload";
    } 

    @RequestMapping("map")
    public String mapCodeText() {
        return "pages/trans/mapCodeMng";
    } 

    @RequestMapping("mapPCodeList")    
    public @ResponseBody String getMapPcodeList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            list = vanDocuService.getMapPcodeList(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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

    @RequestMapping("mapCodeList")    
    public @ResponseBody String getMapCodeList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
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

            list = vanDocuService.getMapCodeList(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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


    @RequestMapping(value = "/insertMapCode", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO insertMapCode(@ModelAttribute("MapCodeVO") @Valid MapCodeVO mapCodeVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	    mapCodeVo.setEnt_user_id(member.getUserId());

            if (vanDocuService.insertMapCode(mapCodeVo)) {
                System.out.println("MapCode Create success");
                result.setResultCode("S000");
                result.setResultMsg("MapCode creation successful.");
            } else {
                System.out.println("chainCreate fail");
                result.setResultCode("F000");
                result.setResultMsg("MapCode creation Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("MapCode creation Failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updateMapCode", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO updateMapCode(@ModelAttribute("MapCodeVO") @Valid MapCodeVO mapCodeVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");            
    	    mapCodeVo.setUpt_user_id(member.getUserId()); 

            if (vanDocuService.updateMapCode(mapCodeVo)) {
                System.out.println("Chain MapCode Update  success");
                result.setResultCode("S000");
                result.setResultMsg("MapCode Update successful.");
            } else {
                System.out.println("Chain MapCode Update  Fail");
                result.setResultCode("F000");
                result.setResultMsg("MapCode Update Failed");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("MapCode Update Failed");
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("list")    
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> totalSumm = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {
            PageingVO pageing = new PageingVO();            
            pageing.setPageingVO(hashmapParam); 

            if (hashmapParam.get("sch_conf_sdt") == null || hashmapParam.get("sch_conf_sdt").toString().isEmpty()) {
                String sch_conf_sdt = commonService.getPreWorkDay();
                hashmapParam.put("sch_conf_sdt", sch_conf_sdt);
            }
            if (hashmapParam.get("sch_conf_edt") == null || hashmapParam.get("sch_conf_edt").toString().isEmpty()) {
                String sch_conf_edt = commonService.getToDay();
                hashmapParam.put("sch_conf_edt", sch_conf_edt);
            }

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

            list = vanDocuService.getScrapDataSumm(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();
            totalSumm = vanDocuService.getScrapDataSummTotal(hashmapParam);
            
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

    @RequestMapping(value = "/getChainDocuSumm", method = RequestMethod.POST)
    public @ResponseBody String getChainDocuSumm(@RequestBody HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        // System.out.println("getChainDocuSummgetChainDocuSummgetChainDocuSummgetChainDocuSummgetChainDocuSumm");
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

            list = vanDocuService.getChainDocuSumm(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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

    @RequestMapping(value = "/getChainVanDocuList", method = RequestMethod.POST)
    public @ResponseBody String getChainVanDocuList(@RequestBody HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> hashmapResult = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        Gson gson = new Gson();
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

            list = vanDocuService.getChainVanDocuList(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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

    @RequestMapping(value = "/downloadDocuListExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadDocuListExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = vanDocuService.getChainVanDocuList(hashmapParam);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Docu List");
            ExcelStyleUtil excelStyle = new ExcelStyleUtil(workbook);

            // Create header row
            Row titleRow = sheet.createRow(0);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("매출 상세 리스트");            
            titleCell.setCellStyle(excelStyle.getStyle("title"));

            // Create header row
            Row headerRow = sheet.createRow(1); 
            String[] headers = {"일련번호"      , "VAN"         , "발급사"          , "매입사"      , "카드번호"
                                , "카드유형"    , "구분"        , "승인번호"        , "승인일시"        , "승인금액"
                                , "취소"        , "원거래일"    , "사전정산"        , "정산상태"        
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
                dataRow.createCell(0).setCellValue(String.valueOf(row.get("docu_seq")));
                dataRow.createCell(1).setCellValue(String.valueOf(row.get("van_cd_nm")));
                dataRow.createCell(2).setCellValue(String.valueOf(row.get("card_iss_nm")));
                dataRow.createCell(3).setCellValue(String.valueOf(row.get("card_acq_nm")));
                dataRow.createCell(4).setCellValue(String.valueOf(row.get("card_no")));
                dataRow.createCell(5).setCellValue(String.valueOf(row.get("card_type_nm")));
                dataRow.createCell(6).setCellValue(String.valueOf(row.get("conf_gb_nm")));
                dataRow.createCell(7).setCellValue(String.valueOf(row.get("conf_no")));
                dataRow.createCell(8).setCellValue(String.valueOf(row.get("conf_dttm")));

                Cell cell = dataRow.createCell(9);
                cell.setCellValue(Double.parseDouble(String.valueOf(row.get("conf_amt"))));
                cell.setCellStyle(excelStyle.getStyle("number"));
                
                dataRow.createCell(10).setCellValue(String.valueOf(row.get("cncl_yn")));
                dataRow.createCell(11).setCellValue(String.valueOf(row.get("org_conf_dt")));
                dataRow.createCell(12).setCellValue(String.valueOf(row.get("proc_fg_nm")));
                dataRow.createCell(13).setCellValue(String.valueOf(row.get("wd_status_nm")));
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
            hHeaders.setContentDispositionFormData("attachment", "DocuList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "/getUnprocessedList", method = RequestMethod.POST)
    public @ResponseBody String getUnprocessedList(@RequestBody HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> hashmapResult = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        Gson gson = new Gson();
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

            list = vanDocuService.getUnprocessedList(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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

    @RequestMapping(value = "/getUnprocessedSumm", method = RequestMethod.POST)
    public @ResponseBody String getUnprocessedSumm(@RequestBody HashMap<String, Object> hashmapParam) {
        HashMap<String, Object> hashmapResult = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        Gson gson = new Gson();
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

            list = vanDocuService.getUnprocessedSumm(hashmapParam);
            int records = vanDocuService.getQueryTotalCnt();

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

    @RequestMapping(value = "/callScrapTransVanDocu", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callScrapTransVanDocu(@ModelAttribute("TransProcessVO") @Valid TransProcessVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());
            
            return vanDocuService.callScrapTransVanDocu(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
        // return result;
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO uploadExcel(@RequestParam("file") MultipartFile file) {
        ReturnDataVO result = new ReturnDataVO();
        List<List<String>> data = new ArrayList<>();
        try {
            if (file.isEmpty()) {
                result.setResultCode("F000");
                result.setResultMsg("File is empty.");
                return result;
            }

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    cell.setCellType(CellType.STRING);
                    rowData.add(cell.getStringCellValue());
                }
                data.add(rowData);
            }
            workbook.close();

            result.setResultCode("S000");
            result.setResultMsg("File processed successfully.");
            result.setData(data);
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the file: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}

