package com.web.manage.system.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.system.domain.BoardVO;

import jakarta.validation.Valid;


@Mapper
public interface BoardMapper {

	int getQueryTotalCnt();
	
	int getParentTotalCnt(String aca_id);
	
	List<HashMap<String, Object>> boardMstListRetrieve(HashMap<String, Object> hashmapParam);
	
	List<HashMap<String, Object>> getSelectBoardFileList(HashMap<String, Object> hashmapParam);
	
	int createBoardSeq();
	
	int getChkFileSeq(BoardVO vo);
	
	int generateFileSeq();
	
	int insertBoardFileInfo(BoardVO vo);
	
	int boardCreate(@Valid BoardVO boardVO);

	int boardUpdate(@Valid BoardVO boardVO);

	int notiCreate(@Valid BoardVO boardVO);

}
