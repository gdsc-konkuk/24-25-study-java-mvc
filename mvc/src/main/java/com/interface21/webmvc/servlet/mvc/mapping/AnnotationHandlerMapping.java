package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handler Mapping 역할을 하는 클래스
 * 일단 현재까지는 AnnotationHandelrMappingTest에서 samples 패키지만 불러옴(TestController 존재)
 * Reflection으로 어노테이션에 맞는 함수를 불러와, HandlerExecution에 등록
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    /**
     * HandlerKey: url, RequestMethod
     * HandlerExecution: return ModelAndView
     */
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    private Set<Class<?>> getAllClassesInPackage(final Object... packageName) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        Reflections reflections = new Reflections(packageName);
        // add all annotated classes
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        log.info("annotated classes: " + annotatedClasses.toString());
        return annotatedClasses;
    }

    /**
     * basePackage에 존재하는 Controller를 어노테이션을 기준으로 handlerExecution 등록
     */
    public void initialize() {
        Set<Class<?>> controllerAnnotatedClasses = getAllClassesInPackage(basePackage);
        for (Class<?> clazz : controllerAnnotatedClasses) {
            for(Method method : clazz.getDeclaredMethods()) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                try{
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance();
                    log.info("requestInfo: " + requestMapping.value());
                    log.info("requestMethod: " + requestMapping.method());
                    log.info("controller: " + instance.getClass().getName());
                    handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()[0]), new HandlerExecution(instance, method));
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    /**
     *
     * @param request request를 기준으로 기존의 HandlerKey에서 일치하는 값을 찾고, 찾게 되면 ExecutionHandler 반환
     * @return
     */
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey targetHandlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        for(Map.Entry<HandlerKey, HandlerExecution> entry : handlerExecutions.entrySet()) {
            HandlerKey handlerKey = entry.getKey();
            if(handlerKey.toString().equals(targetHandlerKey.toString())) {
                return entry.getValue();
            }
        }
        log.info("handler not found!");
        return null;
    }
}