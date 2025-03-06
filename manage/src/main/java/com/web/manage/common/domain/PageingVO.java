package com.web.manage.common.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class PageingVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7227522246573477467L;
	
	/** 현재페이지 */
    private int draw = 1;
    
    /** 페이지갯수 */
    private int total = 0;
    
    /** 전체 갯수 */
    private int records = 0;
    
    /** 전체 갯수 */
    private int start = 0;

    /** 페이지 크기 **/
    private int length = 20;

    private List<HashMap<String, Object>> columns;

    private HashMap<String, Object> search;

    private List<HashMap<String, Object>> order;

    @SuppressWarnings("unchecked")
    public void setPageingVO(HashMap<String, Object> search) {
        this.columns = (List<HashMap<String, Object>>) search.get("columns");
        this.search = (HashMap<String, Object>) search.get("search");
        this.order = (List<HashMap<String, Object>>) search.get("order");
        this.draw = (int) search.get("draw");
        this.length = (int) search.get("length");
        this.start = (int) search.get("start");
    }

}
