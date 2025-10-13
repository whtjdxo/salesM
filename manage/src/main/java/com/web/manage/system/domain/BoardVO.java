package com.web.manage.system.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO implements Serializable{
	
	private static final long serialVersionUID = 7666739011141676564L;
	
	
	private String board_seq;
	private String board_tp;
	private String title;
	private String conts;
	private String use_yn;
	private String top_yn;
	private String pop_yn;
	private String up_mark_yn;
	private String view_corp;
	private String vod_url;
	private String user_id; 
	
	/* 첨부파일 */
	private String file_seq;
	private String file_nm;
	private String origin_file_nm;	
	private String file_path;
	private String file_icon_tp;
	private String file_ext;
	private String file_size;
	List<MultipartFile> file_upload = null;
}
