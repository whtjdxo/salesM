package com.web.manage.deposit.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.deposit.domain.ProcDepositVO;
import com.web.manage.deposit.service.DepositService;

// Removed incorrect import for Cell
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.CommonService;
import com.google.gson.Gson;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*; 

@Controller
@RequestMapping("/deposit/deposit/")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @Autowired
   private CommonService commonService; 

    @RequestMapping("depoMng/view")
    public String view() {
        return "pages/deposit/depositMng";
    }
    @RequestMapping("depoList/view")
    public String listview() {
        return "pages/deposit/depositList";
    }

    @RequestMapping(value = "depoMng/depositSummary", method = RequestMethod.POST)
    public @ResponseBody String getDepositSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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

            // System.out.println(hashmapParam);

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = depositService.getDepositSummary(hashmapParam);
            int records = depositService.getQueryTotalCnt();
            totalSumm = depositService.getDepositSummaryTotal(hashmapParam);

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

    @RequestMapping(value = "depoMng/depoCardSummary", method = RequestMethod.POST)
    public @ResponseBody String getDepoCardSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> depoStatus = new HashMap<String, Object>();
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

            list = depositService.getDepoCardSummary(hashmapParam);
            int records = depositService.getQueryTotalCnt();
            depoStatus = depositService.getChainDepoStatus(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("depoStatus", depoStatus); 

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jString;  
    }

    @RequestMapping(value="depoMng/chainDepositStatus", method = RequestMethod.POST)
	public @ResponseBody ReturnDataVO totalCodelist(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session){
		HashMap<String, Object> depoStatus = new HashMap<String, Object>();
		ReturnDataVO result = new ReturnDataVO();
		try {
			depoStatus = depositService.getChainDepoStatus(hashmapParam); 
			result.setResultCode("S000");
			result.setData(depoStatus);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}

    @RequestMapping(value = "depoMng/chainDepositStatus2", method = RequestMethod.POST)
    public @ResponseBody String getChainDepositStatus(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();         
        HashMap<String, Object> depoStatus = new HashMap<String, Object>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 
        try {            
            depoStatus = depositService.getChainDepoStatus(hashmapParam);   
            hashmapResult.put("depoStatus", depoStatus); 
            jString = gson.toJson(hashmapResult);
            System.out.println("depoStatus : " + depoStatus);
        } catch (Exception e) {
            e.printStackTrace();
       }

        return jString;  
    }

    @RequestMapping(value = "depoMng/depoResvList", method = RequestMethod.POST)
    public @ResponseBody String getDepoResvList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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

            list = depositService.getDepoResvList(hashmapParam);
            int records = depositService.getQueryTotalCnt();

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

    @RequestMapping(value = "depoMng/depoResvExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getDepoResvExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = depositService.getDepoResvList(hashmapParam);

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
            hHeaders.setContentDispositionFormData("attachment", "remitList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @RequestMapping(value = "depoMng/changeResvDate", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcChangeResvDate(@ModelAttribute("ProcDepositVO") @Valid ProcDepositVO procVo, HttpSession session) {

        ReturnDataVO result = new ReturnDataVO(); 
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return depositService.callProcChangeResvDate(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
 
    }

    @RequestMapping(value = "depoMng/procDepositAdjust", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcDepositAdjust(@ModelAttribute("ProcDepositVO") @Valid ProcDepositVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return depositService.callProcDepositAdjust(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
    }

    @RequestMapping(value = "depoList/depoAdjustSummary", method = RequestMethod.POST)
    public @ResponseBody String getDepoAdjustSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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

            // System.out.println(hashmapParam);

            int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
            hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
            hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
            hashmapParam.put("start", pageing.getStart());
            hashmapParam.put("end", pageing.getLength());

            list = depositService.getDepoAdjustSummary(hashmapParam);
            int records = depositService.getQueryTotalCnt();
            totalSumm = depositService.getDepoAdjustSummTotal(hashmapParam);

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

    @RequestMapping(value = "depoList/depoAdjustCardSummary", method = RequestMethod.POST)
    public @ResponseBody String getDepoAdjustCardSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> depoStatus = new HashMap<String, Object>();
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

            list = depositService.getDepoAdjustCardSummary(hashmapParam);
            int records = depositService.getQueryTotalCnt();
            depoStatus = depositService.getChainDepoStatus(hashmapParam);

            pageing.setRecords(records);
            pageing.setTotal((int) Math.ceil((double) records / (double) pageing.getLength()));

            hashmapResult.put("draw", pageing.getDraw());
            hashmapResult.put("recordsTotal", pageing.getRecords());
            hashmapResult.put("recordsFiltered", pageing.getRecords());
            hashmapResult.put("data", list);
            hashmapResult.put("depo", depoStatus);

            jString = gson.toJson(hashmapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;  
    }

    @RequestMapping(value = "depoList/depoAdjustList", method = RequestMethod.POST)
    public @ResponseBody String getDepoAdjustList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
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

            list = depositService.getDepoAdjustList(hashmapParam);
            int records = depositService.getQueryTotalCnt();

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

    @RequestMapping(value = "depoList/depoAdjustExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getDepoAdjustExcel(@RequestBody HashMap<String, Object> hashmapParam) {
        try {
            // Fetch data for the Excel file
            hashmapParam.put("sidx", "");
            hashmapParam.put("sord", "");
            hashmapParam.put("start", "0");
            hashmapParam.put("end", "9999");
            List<HashMap<String, Object>> list = depositService.getDepoAdjustList(hashmapParam);

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
            hHeaders.setContentDispositionFormData("attachment", "remitList.xlsx");

            return ResponseEntity.ok()
                    .headers(hHeaders)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "depoList/procDepoAdjustCancel", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO callProcDepoAdjustCancel(@ModelAttribute("ProcDepositVO") @Valid ProcDepositVO procVo, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO(); 
        try {
            System.out.println("procVo : " + procVo);
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            procVo.setUserId(member.getUserId());            
            return depositService.callProcDepoAdjustCancel(procVo);             
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("An error occurred while processing the scrap transaction.");
            e.printStackTrace();
            return result;
        }
    }
}