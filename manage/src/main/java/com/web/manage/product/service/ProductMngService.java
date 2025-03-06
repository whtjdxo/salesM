package com.web.manage.product.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.product.mapper.ProductMngMapper;

@Service
public class ProductMngService {

    @Autowired
    private ProductMngMapper productMngMapper;

    public int create(HashMap<String, Object> hashmapParamm) {
        return productMngMapper.create(hashmapParamm);
    }

    public boolean imgUrlUpdate(HashMap<String, Object> hashmapParamm) {
        return productMngMapper.imgUrlUpdate(hashmapParamm);
    }

    public List<HashMap<String, Object>> goodsMngListRetrieve(HashMap<String, Object> hashmapParam) {
        return productMngMapper.goodsMngListRetrieve(hashmapParam);
    }

    public int getQueryTotalCnt() {
        return productMngMapper.getQueryTotalCnt();
    }

    public HashMap<String, Object> goodsMngDetailRetrieve(String goods_cd) {
        return productMngMapper.goodsMngDetailRetrieve(goods_cd);
    }
}
