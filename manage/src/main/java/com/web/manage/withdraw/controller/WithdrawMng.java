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
@RequestMapping("/withdraw/withdraw/withdrawMng/")

public class WithdrawMng {
    // static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    // @Autowired
    // private WithdrawService withdrawService; 

    // @RequestMapping("view")
    // public String view() {
    //     return "pages/withdraw/withdrawMng";
    // }

    // @RequestMapping("excel") 
    // public String excelUpload() {
    //     return "pages/withdraw/vanDocuUpload";
    // } 
 

    // @RequestMapping("summary")    
    // public @ResponseBody String getWithdrawSummary(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
    //     HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
    //     List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    //     Gson gson = new Gson();
    //     SessionVO member = (SessionVO) session.getAttribute("S_USER");
    //     hashmapParam.put("user_id", member.getUserId());
    //     String jString = null; 
    //     try {
    //         PageingVO pageing = new PageingVO();
    //         pageing.setPageingVO(hashmapParam);

    //         // System.out.println(hashmapParam);

    //         int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
    //         hashmapParam.put("sidx", pageing.getColumns().get(ordCol).get("data"));
    //         hashmapParam.put("sord", pageing.getOrder().get(0).get("dir"));
    //         hashmapParam.put("start", pageing.getStart());
    //         hashmapParam.put("end", pageing.getLength());

    //         list = withdrawService.getWithdrawSummary(hashmapParam);
    //         int records = withdrawService.getQueryTotalCnt();

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
}