package com.interface21.web.handler.adapter;

import com.interface21.web.ModelAndView;
import com.interface21.web.handler.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response,
            final Object handler) throws Exception {
        if (!supports(handler)) {
            throw new IllegalArgumentException("This handler is not supported: " + handler);
        }

        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
