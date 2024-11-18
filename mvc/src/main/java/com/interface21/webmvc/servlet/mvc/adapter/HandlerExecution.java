package com.interface21.webmvc.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * - HandlerExecution 클래스는 Handler Adapter의 역할을 수행
 * - 이 클래스는 HTTP 요청을 받아 해당하는 컨트롤러의 메서드를 실행하는 역할
 * - AnnotationHandlerMapping에서 제공하는 HandlerKey를 기반으로 동작
 */
public class HandlerExecution implements Handler {
    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object controller;
    private final Method handlerMethod;

    public HandlerExecution(Object controller, Method handlerMethod) {
        this.controller = controller;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object[] args = prepareMethodArguments(request, response);

        try {
            Object result = handlerMethod.invoke(controller, args);
            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new Exception("Handler method invocation failed", e);
        } catch (InvocationTargetException e) {
            throw new Exception("Handler method execution failed", e.getTargetException());
        }
        return null;
    }

    private Object[] prepareMethodArguments(HttpServletRequest request, HttpServletResponse response) {
        return new Object[]{request, response};
    }
}