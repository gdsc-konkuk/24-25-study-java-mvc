package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);
    private final Object controller;
    private final Method handlerMethod;

    public HandlerExecution(Object controller, Method handlerMethod) {
        this.controller = controller;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object[] args = prepareMethodArguments(request, response);
        log.info("contoller method: " + controller.getClass().getName());
        log.info("handler method: " + handlerMethod.getName());
        try {
            Object result = handlerMethod.invoke(controller, args);
            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.error("Error invoking handler method", e);
            throw new Exception("Handler method invocation failed", e);
        } catch (InvocationTargetException e) {
            log.error("Handler method threw an exception", e.getTargetException());
            throw new Exception("Handler method execution failed", e.getTargetException());
        }
        return null;
    }

    private Object[] prepareMethodArguments(HttpServletRequest request, HttpServletResponse response) {
        return new Object[]{request, response};
    }
}
