package com.interface21.web.handler.adapter;

import com.interface21.web.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response,
            final Object handler) throws Exception;
}
