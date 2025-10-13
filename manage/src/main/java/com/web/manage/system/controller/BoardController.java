package com.web.manage.system.controller;

import java.io.File;
import java.nio.file.Paths;
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
import com.web.manage.base.domain.ChainFileVO;
import com.web.manage.common.domain.PageingVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.system.domain.BoardFileVO;
import com.web.manage.system.domain.BoardVO;
import com.web.manage.system.domain.GroupCodeVO;
import com.web.manage.system.service.BoardService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/manage/system/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Value("${global.fileBoardPath}")
	String origin_fileBoardPath;

	@RequestMapping(value="/view")
	public String view() {
		return "pages/system/board";
	}

	/**
	 * 게시판 목록 가져오기
	 * @param hashmapParam
	 * @return json
	 */
	@RequestMapping("/list")
	public @ResponseBody String getBoardList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session){
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

			list = boardService.getBoardList(hashmapParam);
			int records =  boardService.getQueryTotalCnt();

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

	@RequestMapping("/boardFileList")    
    public @ResponseBody String getBoardFileList(@RequestBody HashMap<String, Object> hashmapParam, HttpSession session) {         
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Gson gson = new Gson();
        SessionVO member = (SessionVO) session.getAttribute("S_USER");
        hashmapParam.put("user_id", member.getUserId());
        String jString = null; 

		System.out.println("hashmapParam: " + hashmapParam);

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

            list = boardService.getBoardFileList(hashmapParam);
            int records = boardService.getQueryTotalCnt();

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
			if(result.getResultCode().equals("V999")){
				return result;
			}
			
			String seq = boardService.createBoardSeq();
			boardVO.setBoard_seq(String.valueOf(seq));
			
			// 첨부파일			
			if(boardVO.getFile_upload() != null) {
				if(boardVO.getFile_upload().size() > 0) {					
					for(MultipartFile file : boardVO.getFile_upload()) {						 
						String pathString = Paths.get(origin_fileBoardPath, boardVO.getBoard_seq()).toString();
                        // System.out.println("pathString : " + pathString);    
	                   
                        File saveFolder = new File(pathString);
	                    if(!saveFolder.exists() || saveFolder.isFile()) {
	                        saveFolder.mkdirs();
	                    }
	                    
	                    String originName = file.getOriginalFilename();
	                    String fileSize = String.valueOf(file.getSize());

                        int pos = originName.lastIndexOf(".");
	                    String fileExt = originName.substring(pos + 1);
						
						int file_seq = boardService.getChkFileSeq(boardVO);
	                    // int file_seq = boardService.generateFileSeq();
	                    String newName = boardVO.getBoard_seq() + "_"+ file_seq + Math.round(Math.random() * 100);
	                    
	                    // filePath = File.separator + newName + "." + fileExt;
                        String savePath = pathString + File.separator + newName + "." + fileExt;

                        System.out.println("newName : " + newName);
                        System.out.println("filePath : " + savePath);

	                    file.transferTo(new File(savePath));

	                    // String savePath = "/upload/" + boardService.getFile_chain_no()  + "/" + newName + "." + fileExt;	 
                        System.out.println("savePath : " + savePath); 

	                    // File fileExist = new File(pathString + "/" + newName + "." + fileExt);						
                        File fileExist = new File(savePath);
	                    
	                    // 파일 업로드 확인
	                    if(fileExist.exists()) {
	        			    boardVO.setFile_seq(String.valueOf(file_seq));
	        			    boardVO.setOrigin_file_nm(originName);
	        			    boardVO.setFile_nm(newName);
	        			    boardVO.setFile_path(savePath);	        			    
	        			    boardService.insertBoardFileInfo(boardVO);
	                    }	                    
					}					
				}
			}
			boardVO.setConts(StringEscapeUtils.unescapeHtml4(cnts));
			boardService.boardCreate(boardVO);
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
					if(result.getResultCode().equals("V999")){
						return result;
					}					
					// 첨부파일
					if(boardVO.getFile_upload() != null) {
						if (boardVO.getFile_upload().size() > 0) {		
							for(MultipartFile file : boardVO.getFile_upload()) {						 
								String pathString = Paths.get(origin_fileBoardPath, boardVO.getBoard_seq()).toString();
								// System.out.println("pathString : " + pathString);    
							
								File saveFolder = new File(pathString);
								if(!saveFolder.exists() || saveFolder.isFile()) {
									saveFolder.mkdirs();
								}
								
								String originName = file.getOriginalFilename();
								String fileSize = String.valueOf(file.getSize());

								int pos = originName.lastIndexOf(".");
								String fileExt = originName.substring(pos + 1);
								
								int file_seq = boardService.getChkFileSeq(boardVO);
								// int file_seq = boardService.generateFileSeq();
								String newName = boardVO.getBoard_seq() + "_"+ file_seq + Math.round(Math.random() * 100);
								
								// filePath = File.separator + newName + "." + fileExt;
								String savePath = pathString + File.separator + newName + "." + fileExt;

								System.out.println("newName : " + newName);
								System.out.println("filePath : " + savePath);

								file.transferTo(new File(savePath));

								// String savePath = "/upload/" + boardService.getFile_chain_no()  + "/" + newName + "." + fileExt;	 
								System.out.println("savePath : " + savePath); 

								// File fileExist = new File(pathString + "/" + newName + "." + fileExt);						
								File fileExist = new File(savePath);
								
								// 파일 업로드 확인
								if(fileExist.exists()) {
									boardVO.setFile_seq(String.valueOf(file_seq));
									boardVO.setOrigin_file_nm(originName);
									boardVO.setFile_nm(newName);
									boardVO.setFile_path(savePath);	        			    
									boardService.insertBoardFileInfo(boardVO);
								}	                    
							}	
						}
					}
		
					boardService.boardUpdate(boardVO);		
					result.setResultCode("S000");
		
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.setResultCode("S999");
					result.setResultMsg("에러가 발생하였습니다.");
				}
				return result;
    }

	@RequestMapping(value="/deleteFile", method= RequestMethod.POST)
    public @ResponseBody ReturnDataVO deleteBoardFile(@ModelAttribute("boardFileVo") @Valid BoardFileVO boardFileVO, BindingResult bindResult, HttpSession session){
    	ReturnDataVO result = new ReturnDataVO();
    	SessionVO member = (SessionVO) session.getAttribute("S_USER");
    	boardFileVO.setUser_id(member.getUserId());
		try {			
			result = ValidateUtil.validCheck(bindResult, result);
			if(result.getResultCode().equals("V999")){
				return result;
			}
			if(boardService.deleteBoardFile(boardFileVO) == 1){
				result.setResultCode("S000");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode("S999");
			result.setResultMsg(null);
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
