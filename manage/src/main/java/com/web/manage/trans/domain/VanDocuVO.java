package com.web.manage.trans.domain;

import lombok.Data;

@Data
public class VanDocuVO {
    private String docu_seq;
    private String chain_no;
    private String van_cd;
    private String docu_type;
    private String recv_date;
    private String recv_time;
    private String card_reg_no;
    private String trans_id;
    private String term_id;
    private String card_iss;
    private String card_acq;
    private String card_no;
    private String install_mon;
    private String conf_no;
    private String conf_gb;
    private String conf_dt;
    private String conf_tm;
    private String conf_amt;
    private String depo_resv_amt;
    private String depo_resv_dt;
    private String card_type;
    private String org_conf_dt;
    private String cncl;
    private String cncl_docu_seq;
    private String cncl_user_id;
    private String cncl_date;
    private String cncl_time;
    private String dup;
    private String dup_docu_seq;
    private String proc_fg;
    private String proc_result_cd;
    private String proc_result;
    private String proc_date;
    private String re_proc;
    private String re_proc_date;
    private String re_proc_user_id;
    private String re_proc_user_name;
    private String sc_seq;
    private String ent_user_id;
    private String ent_dttm;
    private String upt_user_id;
    private String upt_dttm;
}
