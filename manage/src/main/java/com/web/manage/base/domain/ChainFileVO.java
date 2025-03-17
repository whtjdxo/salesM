package com.web.manage.base.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ChainFileVO {
    private String file_seq;
    private String chain_no;
    private String file_chain_no;
    private String file_gb;
    private String file_nm;
    private String file_path;
    private String origin_file_nm;  
    private String file_icon_tp;
    
	private String file_ext;
	private String file_size;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;
    List<MultipartFile> file_upload = null;
}
