package com.web.manage.base.domain;

import lombok.Data;

@Data
public class ChainCardVO {
    private String card_seq;
    private String chain_no;
    private String card_chain_no;
    private String card_acq;
    private String card_reg_no;
    private String card_rate;
    private String check_rate;
    private String abroad_rate;
    private String card_in_cycle;
    private String card_memo;
    private String depo_chk;
    private String card_use_yn;
    private String use_yn;
    private String ent_dttm;
    private String ent_user_id;
    private String upt_dttm;
    private String upt_user_id;
}
