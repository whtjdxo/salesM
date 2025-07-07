package com.web.manage.students.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.web.common.util.StringUtil;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.students.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student/students/studentMng")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/view")
    public String view() {
        return "pages/students/student";
    }
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

			hashmapParam.put("srch_center", StringUtil.nullCheck((String) pageing.getSearch().get("srch_center"), "") );
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

            list = studentService.studentsListRetrieve(hashmapParam);
            int records =  studentService.getQueryTotalCnt();

			 pageing.setRecords(records);
			 pageing.setTotal((int) Math.ceil((double)records / (double)pageing.getLength()));


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
	@RequestMapping("/create")
	public @ResponseBody ReturnDataVO create(@RequestParam HashMap<String, Object> hashmapParam, HttpSession session) {
		HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		hashmapParam.put("user_id", member.getUserId());
		ReturnDataVO result = new ReturnDataVO();
		try {
			if(studentService.stdIdCheck(hashmapParam) > 0){
				result.setResultCode("F000");
				result.setResultMsg("학생등록에 실패하였습니다.");
				return result;
			}
			if(studentService.studentsCreate(hashmapParam)){
				result.setResultCode("S000");
			} else {
				result.setResultCode("F000");
				result.setResultMsg("학생등록에 실패하였습니다.");
			}

			hashmapResult.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/update")
	public @ResponseBody ReturnDataVO update(@RequestParam HashMap<String, Object> hashmapParam, HttpSession session) {
		HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		hashmapParam.put("user_id", member.getUserId());
		ReturnDataVO result = new ReturnDataVO();
		try {
			if(studentService.studentsUpdate(hashmapParam)){
				result.setResultCode("S000");
			} else {
				result.setResultCode("F000");
				result.setResultMsg("학생수정에 실패하였습니다.");
			}

			hashmapResult.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
