package com.interface21.web.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean supports(final HttpServletRequest request);

    Object getHandler(final HttpServletRequest request);
}
