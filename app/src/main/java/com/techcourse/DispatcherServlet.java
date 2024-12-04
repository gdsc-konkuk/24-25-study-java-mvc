package com.techcourse;

import ch.qos.logback.core.model.Model;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Handler;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapters;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerAdapters = new HandlerAdapterRegistry();
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init(){
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final Object handler = handlerMappingRegistry.getHandler(request);
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdater(handler);

        handleRequest(request, response, handler, handlerAdapter);
    }

    private void handleRequest(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler, final HandlerAdapter handlerAdapter) {
        final ModelAndView modelAndView;
        try {
            modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.renderView(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
