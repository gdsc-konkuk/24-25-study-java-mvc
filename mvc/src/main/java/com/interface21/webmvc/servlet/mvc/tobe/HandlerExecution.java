package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method handlerMethod;

    public HandlerExecution(Class<?> controllerClass, Method handlerMethod) {
        this.controllerClass = controllerClass;
        this.handlerMethod = handlerMethod;
    }
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        return (ModelAndView) handlerMethod.invoke(controllerInstance, request, response);
    }
}
