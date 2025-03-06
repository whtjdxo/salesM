package com.web.config.interceptor;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.lang.NonNull;

@Component
public class AuthInterceptor implements HandlerInterceptor {
	static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

	@SuppressWarnings("null")
	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @NonNull ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}


	@SuppressWarnings("unused")
	@Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();

        if (session == null) {
            logger.debug((String) request.getAttribute("S_LOGIN_YN"));
            logger.debug("##################isLogon : XXXXXXX");
            response.sendRedirect("/login");
			return false;
        }

        return true; // Return true to continue the request, false to block it
    }
}