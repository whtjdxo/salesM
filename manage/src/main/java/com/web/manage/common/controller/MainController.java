package com.web.manage.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.manage.common.service.MainService;


@Controller
public class MainController {

	@Autowired
	private MainService mapper;

	@RequestMapping(value="/main")
	public String mainPage() {

		return "pages/main";
	}

}
