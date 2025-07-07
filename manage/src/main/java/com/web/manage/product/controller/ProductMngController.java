package com.web.manage.product.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.web.common.util.DateUtil;
import com.web.common.util.StringUtil;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.product.service.ProductMngService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/product/product/")
public class ProductMngController {

    @Autowired
    private ProductMngService productMngService;

    @Value("${global.fileStorePath}")
    String origin_fileStorePath;

    @RequestMapping("productMng/view")
    public String view() {
        return "pages/product/product";
    }

    @RequestMapping("productAddMng/view")
    public String addView() {
        return "pages/product/productAdd";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("productMng/detail/{goods_cd}")
    public ModelAndView detail(@PathVariable("goods_cd") String goods_cd) {
        ModelAndView mav = new ModelAndView();

        try {
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap = productMngService.goodsMngDetailRetrieve(goods_cd);

            List<HashMap<String, Object>> chapter_info = new ObjectMapper().readValue(
                    (String) resultMap.get("chapter_info"), new TypeReference<List<HashMap<String, Object>>>() {
                    });

            List<String> requirements_info = new ObjectMapper().readValue(
                    (String) resultMap.get("requirements_info"), new TypeReference<List<String>>() {
                    });

            List<String> goals_info = new ObjectMapper().readValue(
                    (String) resultMap.get("goals_info"), new TypeReference<List<String>>() {
                    });

            mav.addObject("goods_info", resultMap);
            mav.addObject("chapter_info", chapter_info);
            mav.addObject("requirements_info", requirements_info);
            mav.addObject("goals_info", goals_info);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.setViewName("pages/product/productDetail");
        return mav;
    }

    @RequestMapping(value = "productAddMng/list", method = RequestMethod.POST)
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

            list = productMngService.goodsMngListRetrieve(hashmapParam);
            int records = productMngService.getQueryTotalCnt();

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

    @RequestMapping(value = "productAddMng/create", method = RequestMethod.POST)
    public @ResponseBody ReturnDataVO create(@RequestBody HashMap<String, Object> hashmapParamm, HttpSession session) {
        ReturnDataVO returnDataVO = new ReturnDataVO();
        try {
            if (productMngService.create(hashmapParamm) == 1) {
                returnDataVO.setReturnVal(String.valueOf(hashmapParamm.get("goods_cd")));
                returnDataVO.setResultCode("S000");
                returnDataVO.setResultMsg("등록되었습니다.");
            }
        } catch (Exception e) {
            returnDataVO.setResultCode("F000");
            returnDataVO.setResultMsg("등록에 실패하였습니다.");
            e.printStackTrace();
        }
        return returnDataVO;
    }

    @RequestMapping(value = "productAddMng/uploadImage")
    public @ResponseBody ReturnDataVO uploadImage(MultipartRequest request, HttpSession session,
            @RequestParam("goods_cd") String goods_cd) {
        ReturnDataVO returnDataVO = new ReturnDataVO();
        try {
            MultipartFile file = request.getFile("file");
            String savePath = fileUpload(file);
            HashMap<String, Object> hashmapParamm = new HashMap<String, Object>();
            hashmapParamm.put("image_url", savePath);
            hashmapParamm.put("goods_cd", goods_cd);
            if (productMngService.imgUrlUpdate(hashmapParamm)) {
                returnDataVO.setResultCode("S000");
                returnDataVO.setResultMsg("등록되었습니다.");
            }
        } catch (Exception e) {
            returnDataVO.setResultCode("F000");
            returnDataVO.setResultMsg("등록에 실패하였습니다.");
        }
        return returnDataVO;
    }

    public String fileUpload(MultipartFile file) {
        String date = (DateUtil.getTodateYmd().replaceAll("-", ""));
        String pathString = origin_fileStorePath + "goods/" + date;
        String filePath = "";
        String savePath = "";
        try {
            File saveFolder = new File(pathString);
            if (!saveFolder.exists() || saveFolder.isFile()) {
                saveFolder.mkdirs();
            }
            File dateFolder = new File(pathString);
            if (!dateFolder.exists() || dateFolder.isFile()) {
                dateFolder.mkdirs();
            }
            System.out.println(file.getOriginalFilename());
            String originName = file.getOriginalFilename();
            int pos = originName.lastIndexOf(".");
            String fileExt = originName.substring(pos + 1);
            String newName = "goods" + date + Math.round(Math.random() * 100);
            filePath = dateFolder + File.separator + newName + "." + fileExt;
            file.transferTo(new File(filePath));

            File fileExist = new File(pathString + "/" + newName + "." + fileExt);

            if (fileExist.exists()) {
                savePath = "/upload/goods/" + date + "/" + newName + "." + fileExt;
            }
        } catch (Exception e) {
            return null;
        }
        return savePath;
    }
}
