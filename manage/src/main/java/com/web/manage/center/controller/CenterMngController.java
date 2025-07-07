package com.web.manage.center.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.web.common.util.StringUtil;
import com.web.manage.center.service.CenterMngService;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/center/center/centerMng")
public class CenterMngController {

    @Autowired
    private CenterMngService centerMngService;

    @RequestMapping("/view")
    public String requestMethodName() {
        return "pages/center/centerMng";
    }

    @Value("${global.fileStorePath}")
    String origin_fileStorePath;

    @RequestMapping("/list")
    public @ResponseBody String list(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null;
        try {
            PageingVO pageing = new PageingVO();
            pageing.setPageingVO(hashmapParam);

            hashmapParam.put("srch_center", StringUtil.nullCheck((String) pageing.getSearch().get("srch_center"), ""));
            hashmapParam.put("srch_brd_tp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_brd_tp"), ""));
            hashmapParam.put("srch_tp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_tp"), ""));
            hashmapParam.put("srch_tp_inp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_tp_inp"), ""));
           
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

            list = centerMngService.centerMngListRetrieve(hashmapParam);
            int records = centerMngService.getQueryTotalCnt();

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

    @RequestMapping("getBossInfo")
    public @ResponseBody ReturnDataVO getBossInfo(@RequestParam String boss_id) {
        ReturnDataVO result = new ReturnDataVO();
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        try {
            hashmapResult = centerMngService.getBossInfo(boss_id);
            result.setData(hashmapResult);
            result.setResultCode("S000");
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("원장정보를 불러오지못했습니다.");
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO create(@RequestParam HashMap<String, Object> hashmapParam, HttpSession session,
            MultipartHttpServletRequest request) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            hashmapParam.put("ent_user_id", member.getUserId());
            String aca_id = (String) hashmapParam.get("aca_id");
            hashmapParam.put("logo_img", fileUpload(request.getFile("logo_img"), aca_id, "logo"));
            hashmapParam.put("favicon_img", fileUpload(request.getFile("favicon_img"), aca_id, "favicon"));

            if (centerMngService.centerCreate(hashmapParam)) {
                if (centerMngService.bossCreate(hashmapParam)) {
                    result.setResultCode("S000");
                } else {
                    result.setResultCode("F000");
                    result.setResultMsg("원장등록에 실패하였습니다.");
                }
            } else {
                result.setResultCode("F000");
                result.setResultMsg("센터등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("등록에 실패하였습니다.");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO update(@RequestParam HashMap<String, Object> hashmapParam, HttpSession session,
            MultipartHttpServletRequest request) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            SessionVO member = (SessionVO) session.getAttribute("S_USER");
            hashmapParam.put("ent_user_id", member.getUserId());
            String aca_id = (String) hashmapParam.get("aca_id");
            hashmapParam.put("logo_img", fileUpload(request.getFile("logo_img"), aca_id, "logo"));
            hashmapParam.put("favicon_img", fileUpload(request.getFile("favicon_img"), aca_id, "favicon"));
            if (centerMngService.centerUpdate(hashmapParam)) {
                if (centerMngService.bossUpdate(hashmapParam)) {
                    result.setResultCode("S000");
                } else {
                    result.setResultCode("F000");
                    result.setResultMsg("원장등록에 실패하였습니다.");
                }
            } else {
                result.setResultCode("F000");
                result.setResultMsg("센터등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("등록에 실패하였습니다.");
            e.printStackTrace();
        }
        return result;
    }

    public String fileUpload(MultipartFile file, String aca_id, String file_gb) {

        String pathString = origin_fileStorePath + "logos/" + aca_id;
        String filePath = "";
        String savePath = "";
        try {
            File saveFolder = new File(pathString);
            if (!saveFolder.exists() || saveFolder.isFile()) {
                saveFolder.mkdirs();
            }
            File acaIdFolder = new File(pathString);
            if (!acaIdFolder.exists() || acaIdFolder.isFile()) {
                acaIdFolder.mkdirs();
            }
            System.out.println(file.getOriginalFilename());
            String originName = file.getOriginalFilename();
            if (originName == null) {
                throw new IllegalArgumentException("File name cannot be null");
            }
            int pos = originName.lastIndexOf(".");
            String fileExt = originName.substring(pos + 1);
            String newName = "logos" + "_" + aca_id + "_" + file_gb;
            filePath = acaIdFolder + File.separator + newName + "." + fileExt;
            file.transferTo(new File(filePath));

            File fileExist = new File(pathString + "/" + newName + "." + fileExt);

            if (fileExist.exists()) {
                savePath = "/upload/logos/" + aca_id + "/" + newName + "." + fileExt;
            }
        } catch (Exception e) {
            return null;
        }
        return savePath;
    }
}
