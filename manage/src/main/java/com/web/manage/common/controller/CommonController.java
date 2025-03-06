package com.web.manage.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.service.CommonService;

@Controller
@RequestMapping(value="/common/")
public class CommonController {
    
    @Autowired
    private CommonService commonService;

    /**
	 * 화면에 쓸 전체 코드 목록 가져오기
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/totalCodelist")
	public @ResponseBody ReturnDataVO totalCodelist(@RequestParam HashMap<String, String> hashmapParam){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		ReturnDataVO result = new ReturnDataVO();
		try {
			list = commonService.getTotalCodelist(hashmapParam);
			result.setResultCode("S000");
			result.setData(list);
		} catch (Exception e) {
			result.setResultMsg(null);
			result.setResultCode("S999");
			e.printStackTrace();
		}
		return result;
	}
}
