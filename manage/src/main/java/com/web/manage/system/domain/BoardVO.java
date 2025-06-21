package com.web.manage.system.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO implements Serializable{
	
	private static final long serialVersionUID = 7666739011141676564L;
	
	private String user_id;
	private String p_aca_id;
	private String aca_id;
	
	private String board_seq;
	private String board_tp;
	private String view_corp;
	private String title;
	private String conts;
	private String use_yn;
	private String img_url;
	private String vod_url;
	
	/* 첨부파일 */
	private String file_seq;
	private String origin_file_name;
	private String file_name;
	private String file_path;
	private String file_ext;
	private String file_size;
	List<MultipartFile> board_upload = null;
}
