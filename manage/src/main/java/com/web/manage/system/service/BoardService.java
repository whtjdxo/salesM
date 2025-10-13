package com.web.manage.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.system.domain.BoardFileVO;
import com.web.manage.system.domain.BoardVO;
import com.web.manage.system.mapper.BoardMapper;

import jakarta.validation.Valid;


@Service
public class BoardService {

	@Autowired
	private BoardMapper mapper;

	public int getQueryTotalCnt() {
		return mapper.getQueryTotalCnt();
	}
	
	public int getParentTotalCnt(String aca_id) {
		return mapper.getParentTotalCnt(aca_id);
	}
	
	public List<HashMap<String, Object>> getBoardList(HashMap<String, Object> hashmapParam) {
		return mapper.getBoardList(hashmapParam);
	}
	
	public List<HashMap<String, Object>> getBoardFileList(HashMap<String, Object> hashmapParam) {
		return mapper.getBoardFileList(hashmapParam);
	}
	
	public String createBoardSeq() {
		return mapper.createBoardSeq();
	}
	
	public int getChkFileSeq(BoardVO vo) {
		return mapper.getChkFileSeq(vo);
	}
	
	public int generateFileSeq() {
		return mapper.generateFileSeq();
	}
	
	public int insertBoardFileInfo(BoardVO vo) {
		return mapper.insertBoardFileInfo(vo);
	}

	public int deleteBoardFile(BoardFileVO vo) {
		return mapper.deleteBoardFile(vo);
	}
	
	public int boardCreate(@Valid BoardVO boardVO) {
		// TODO Auto-generated method stub
		return mapper.boardCreate(boardVO);
	}

	public int boardUpdate(@Valid BoardVO boardVO) {
		// TODO Auto-generated method stub
		return mapper.boardUpdate(boardVO);
	}
}
