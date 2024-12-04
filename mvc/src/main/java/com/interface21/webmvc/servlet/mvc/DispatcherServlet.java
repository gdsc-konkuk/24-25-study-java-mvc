package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public class DispatcherServlet implements HandlerMapping {
    private final HandlerMapping AnnotationHandlerMapping;
    private final HandlerMapping ManualHandlerMapping;

    public DispatcherServlet(HandlerMapping handlerMapping1, HandlerMapping handlerMapping2) {
        AnnotationHandlerMapping = handlerMapping1;
        ManualHandlerMapping = handlerMapping2;
    }

    /**
     * 원래 아래 두 메서드 중 하나만 있어야 하는데,
     * Annotation 과 Manual 둘 다 사용 가능하게 하기 위해선 이 방법이..?
     */
    public Map<String, Object> doDispatchWithAnnotationHandlerMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var handlerExecution = (HandlerExecution) AnnotationHandlerMapping.getHandler(request);
        var modelAndView=handlerExecution.handle(request,response);
        return modelAndView.getModel();
    }
    public Map<String, Object> doDispatchWithManualHandlerMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var handlerExecution = (HandlerExecution) ManualHandlerMapping.getHandler(request);
        var modelAndView=handlerExecution.handle(request,response);
        return modelAndView.getModel();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}
