package com.web.manage.product.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMngMapper {

    int create(HashMap<String, Object> hashmapParamm);

    boolean imgUrlUpdate(HashMap<String, Object> hashmapParamm);

    List<HashMap<String, Object>> goodsMngListRetrieve(HashMap<String, Object> hashmapParam);

    int getQueryTotalCnt();

    HashMap<String, Object> goodsMngDetailRetrieve(String goods_cd);
}
