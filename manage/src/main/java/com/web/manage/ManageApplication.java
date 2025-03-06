package com.web.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"com.web"})
@MapperScan("com.web.manage.*.mapper")
public class ManageApplication extends SpringBootServletInitializer {
	private static Class<ManageApplication> applicationClass = ManageApplication.class;
	public static void main(String[] args) {
		SpringApplication.run(ManageApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

}
