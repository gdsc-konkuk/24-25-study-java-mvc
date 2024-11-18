package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            // 패키지 스캔으로 Controller를 가진 클래스들 반환
            for (Object packageName : basePackage) {
                Reflections reflections = new Reflections(packageName);
                Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

                // @Controller 클래스 처리
                for (Class<?> controllerClass : controllerClasses) {
                    Method[] methods = controllerClass.getDeclaredMethods();

                    // @RequestMapping 메서드 처리
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
//                    핸들러 정상 등록 확인 (HandlerKey에도 getter 주석 제거해줘야 함)
//                     handlerExecutions.entrySet().stream().map(Map.Entry::getKey)
//                             .map(HandlerKey::getRequestMethod).forEach(System.out::println);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize AnnotationHandlerMapping", e);
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
