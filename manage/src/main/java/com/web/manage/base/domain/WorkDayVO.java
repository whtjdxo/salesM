package com.web.manage.base.domain; 

import lombok.Data;

@Data
public class WorkDayVO {
    
    private String work_date;
    private String day_nm;
    private String working; 
    private String upt_dttm;
    private String upt_user_id;

}
