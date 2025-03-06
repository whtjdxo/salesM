package com.web.manage.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.common.mapper.MainMapper;


@Service
public class MainService {

	@Autowired
	private MainMapper mapper;

	public int getQueryTotalCnt() {
		// TODO Auto-generated method stub
		return mapper.getQueryTotalCnt();
	}

}
