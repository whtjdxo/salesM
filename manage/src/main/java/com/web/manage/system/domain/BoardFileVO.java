package com.web.manage.system.domain;

import lombok.Data;

@Data
public class BoardFileVO {
    private String board_seq;
    private String file_seq;
    private String file_nm;
	private String origin_file_nm;	
	private String file_path;
	private String file_icon_tp;
	private String file_ext;
	private String file_size;
    private String user_id;
}
