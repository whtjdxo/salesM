package com.web.manage.api.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppApiMapper {

	HashMap<String, Object> appApiGetStoreInfo(HashMap<String, Object> params);

	HashMap<String, Object> appApiGetDailySummary(HashMap<String, Object> params);

	List<HashMap<String, Object>> appApiGetDailySalesInfo(HashMap<String, Object> params);

	HashMap<String, Object> appApiGetDailySettleInfo(HashMap<String, Object> params);

	List<HashMap<String, Object>> appApiGetUndepoSubList(HashMap<String, Object> params);

	List<HashMap<String, Object>> appApiGetSubList(HashMap<String, Object> params);

	HashMap<String, Object> appApiGetMonthlySummary(HashMap<String, Object> params);

	List<HashMap<String, Object>> appApiGetMonthlySalesList(HashMap<String, Object> params);
}
