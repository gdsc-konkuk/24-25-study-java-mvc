package com.interface21;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerAdapterRegistry {

    public HandlerAdapterRegistry() {
    }

    public void getHandlerExecution(Method[] methods, Map<HandlerKey, HandlerExecution> handlerExecutions) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();
                RequestMethod[] httpMethod = requestMapping.method();
                // HTTP 메서드별로 모두 핸들러 등록
                for (RequestMethod requestMethod : httpMethod) {
                    HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution();
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }
}
