package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    boolean supports(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}
