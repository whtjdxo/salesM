package com.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.web.config.interceptor.AuthInterceptor;

import org.springframework.lang.NonNull;

@Configuration
@ComponentScan("com.web")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final int BROWSER_CACHE_CONTROL = 604800;

    @Value("${global.fileStorePath}")
    String filePath;
    @Autowired
    AuthInterceptor authInterceptor;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

        registry.addResourceHandler(
                "/data/**",
                "/img/**",
                "/css/**",
                "/js/**",
                "/logos/**",
                "/video/**",
                "/vendors/**")
                .addResourceLocations(
                        "classpath:/static/data/",
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/logos/",
                        "classpath:/static/video/",
                        "classpath:/static/vendors/");
        if ("real".equals(activeProfile)) {
            registry.addResourceHandler("/upload/**").addResourceLocations("file://" + filePath)
                    .setCachePeriod(BROWSER_CACHE_CONTROL).resourceChain(true).addResolver(new PathResourceResolver());
        } else {
            registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/static/upload/");
        }

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/main");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).excludePathPatterns("/login", "/chkLogin", "/register",
                "/registerProcess", "/error", "/main", "/logout", "/upload/**", "/js/**", "/css/**", "/img/**",
                "/vendors/**", "/data/**", "/logos/**", "/video/**", "/api/**", "/app/api/**");
        ;
    }
}
