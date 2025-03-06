package com.web.manage.system.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;


import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.web.common.util.DateUtil;
import com.web.common.util.StringUtil;
import com.web.common.util.ValidateUtil;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.system.domain.BoardVO;
import com.web.manage.system.service.BoardService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/manage/system/board")
public class BoardController {

	@Autowired
	private BoardService mapper;
	
	@Value("${global.fileStorePath}")
	String origin_fileStorePath;

	@RequestMapping(value="/view")
	public String view() {
		return "pages/system/board";
	}

	/**
	 * 게시판 목록 가져오기
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping(value="/list")
	public @ResponseBody String boardMstListRetrieve(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session){
		HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Gson gson = new Gson(); 
		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		hashmapParam.put("user_id", member.getUserId());
		String jString = null;
		
		try {
			PageingVO pageing = new PageingVO();
			pageing.setPageingVO(hashmapParam);
			HashMap<String, Object> searchMap = new HashMap<String, Object>();

			searchMap.put("srch_center", StringUtil.nullCheck((String) pageing.getSearch().get("srch_center"), "") );
			searchMap.put("srch_brd_tp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_brd_tp"), ""));
			searchMap.put("srch_tp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_tp"), ""));
			searchMap.put("srch_tp_inp", StringUtil.nullCheck((String) pageing.getSearch().get("srch_tp_inp"), ""));
			int ordCol = Integer.parseInt(String.valueOf(pageing.getOrder().get(0).get("column")));
			searchMap.put("sidx", pageing.getColumns().get(ordCol).get("data"));
			searchMap.put("sord", pageing.getOrder().get(0).get("dir"));
			
			searchMap.put("start", pageing.getStart());
			searchMap.put("end", pageing.getLength());

			list = mapper.boardMstListRetrieve(searchMap);
			 int records =  mapper.getQueryTotalCnt();

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

	/**
	 * 게시판 등록
	 * @param hashmapParam
	 * @return json
	 */
	@SuppressWarnings("deprecation")
	@Transactional
	@RequestMapping(value="/boardCreate", method= RequestMethod.POST)
	public@ResponseBody ReturnDataVO boardCreate(@ModelAttribute("BoardVO") @Valid BoardVO boardVO, BindingResult bindResult, MultipartHttpServletRequest multiRequest, HttpSession session){

		ReturnDataVO result = new ReturnDataVO();

		SessionVO member = (SessionVO) session.getAttribute("S_USER");
		boardVO.setUser_id(member.getUserId());

		try {
			String cnts = boardVO.getConts();

			result = ValidateUtil.validCheck(bindResult, result);
			
			/*
			if(result.getResultCode().equals("V999")){
				return result;
			}
			*/
			
			int seq = mapper.createBoardSeq();
			boardVO.setBoard_seq(String.valueOf(seq));
			
			// 첨부파일
			if(boardVO.getBoard_upload() != null) {
				if(boardVO.getBoard_upload().size() > 0) {
					
					for(MultipartFile file : boardVO.getBoard_upload()) {
						
						String imgUrl = "";
	                    String filePath = "";
	                    String date = (DateUtil.getTodateYmd().replaceAll("-", ""));
	                    String pathString = origin_fileStorePath + "board/" + date ;
	                    
	                    File saveFolder = new File(pathString);
	                    if(!saveFolder.exists() || saveFolder.isFile()) {
	                        saveFolder.mkdirs();
	                    }

	                    File dateFolder = new File(pathString);
	                    if(!dateFolder.exists() || dateFolder.isFile()) {
	                        dateFolder.mkdirs();
	                    }
	                    
	                    String originName = file.getOriginalFilename();
	                    int pos = originName.lastIndexOf(".");
	                    String fileExt = originName.substring(pos + 1);
	                    String newName = "uploadImage_" + date + Math.round(Math.random() * 100);
	                    String fileSize = String.valueOf(file.getSize());
	                    filePath = dateFolder + File.separator + newName + "." + fileExt;
	                    file.transferTo(new File(filePath));
	                    String savePath = "/upload/board/" + date + "/" + newName + "." + fileExt;
	                    
	                    File fileExist = new File(pathString + "/" + newName + "." + fileExt);
						
	                    
	                    // 파일 업로드 확인
	                    if(fileExist.exists()) {
	                    	// int file_seq = mapper.getChkFileSeq(boardVO);
	                    	int file_seq = mapper.generateFileSeq();
	        			    
	        			    boardVO.setFile_seq(String.valueOf(file_seq));
	        			    boardVO.setOrigin_file_name(originName);
	        			    boardVO.setFile_name(newName);
	        			    boardVO.setFile_path(savePath);
	        			    
	        			    mapper.insertBoardFileInfo(boardVO);
	                    }
	                    
					}
					
				}
			}
			
			

			boardVO.setConts(StringEscapeUtils.unescapeHtml4(cnts));

			mapper.boardCreate(boardVO);

			result.setResultCode("S000");

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.setResultCode("S999");
			result.setResultMsg("에러가 발생하였습니다.");
		}
    	return result;
    }

	/**
	 * 게시판 수정
	 * @param hashmapParam
	 * @return json
	 */
	@SuppressWarnings("deprecation")
	@Transactional
	@RequestMapping(value="/boardUpdate", method= RequestMethod.POST)
	public@ResponseBody ReturnDataVO boardUpdate(@ModelAttribute("BoardVO") @Valid BoardVO boardVO, BindingResult bindResult
			, MultipartHttpServletRequest multiRequest, HttpSession session){

				ReturnDataVO result = new ReturnDataVO();

				SessionVO member = (SessionVO) session.getAttribute("S_USER");
				boardVO.setUser_id(member.getUserId());
		
				try {
					String cnts = boardVO.getConts();
					boardVO.setConts(StringEscapeUtils.unescapeHtml4(cnts));
		
					result = ValidateUtil.validCheck(bindResult, result);
		
					/*
					if(result.getResultCode().equals("V999")){
						return result;
					}
					*/
					
					// 첨부파일
					if(boardVO.getBoard_upload() != null) {
						if (boardVO.getBoard_upload().size() > 0) {
		
							for (MultipartFile file : boardVO.getBoard_upload()) {
		
								String imgUrl = "";
								String filePath = "";
								String date = (DateUtil.getTodateYmd().replaceAll("-", ""));
								String pathString = origin_fileStorePath + "board/" + date;
		
								File saveFolder = new File(pathString);
								if (!saveFolder.exists() || saveFolder.isFile()) {
									saveFolder.mkdirs();
								}
		
								File dateFolder = new File(pathString);
								if (!dateFolder.exists() || dateFolder.isFile()) {
									dateFolder.mkdirs();
								}
		
								String originName = file.getOriginalFilename();
								int pos = originName.lastIndexOf(".");
								String fileExt = originName.substring(pos + 1);
								String newName = "uploadFile_" + date + Math.round(Math.random() * 100);
								String fileSize = String.valueOf(file.getSize());
								filePath = dateFolder + File.separator + newName + "." + fileExt;
								file.transferTo(new File(filePath));
								String savePath = "/upload/board/" + date + "/" + newName + "." + fileExt;
		
								File fileExist = new File(pathString + "/" + newName + "." + fileExt);
		
								// 파일 업로드 확인
								if (fileExist.exists()) {
									// int file_seq = mapper.getChkFileSeq(boardVO);
									int file_seq = mapper.generateFileSeq();
		
									boardVO.setFile_seq(String.valueOf(file_seq));
									boardVO.setOrigin_file_name(originName);
									boardVO.setFile_name(newName);
									boardVO.setFile_path(savePath);
		
									mapper.insertBoardFileInfo(boardVO);
								}
		
							}
		
						}
					}
		
					mapper.boardUpdate(boardVO);
		
					result.setResultCode("S000");
		
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.setResultCode("S999");
					result.setResultMsg("에러가 발생하였습니다.");
				}
				return result;
    }

	public static String unescape(String src){

		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());

		int lastPos = 0, pos = 0;

		char ch;

		while (lastPos < src.length()) {

			pos = src.indexOf("%", lastPos);

			if (pos == lastPos) {

				if (src.charAt(pos + 1) == 'u') {

					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;

				}else {

					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;

				}

			}else {

				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();

				}else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}

		return tmp.toString();
	 }
}
