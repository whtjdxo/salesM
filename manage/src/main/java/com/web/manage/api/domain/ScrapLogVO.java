package com.web.manage.api.domain;

import lombok.Data;

@Data
public class ScrapLogVO {
    private String chain_no;
    private String scrap_gb;
    private String van_cd;
    private String last_scrap_dttm;
    private String upload_cnt;
    private String save_cnt;
    private String dup_cnt;
    private String rslt_msg;
}
