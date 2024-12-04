package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object object;
    private final Method method;

    public HandlerExecution(final Object object, final Method method){
        this.object = object;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
       return (ModelAndView) method.invoke(object, request, response);
    }
}
