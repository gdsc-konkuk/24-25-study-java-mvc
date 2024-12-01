package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.ControllerScanner;
import com.interface21.HandlerAdapterRegistry;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
            ControllerScanner controllerScanner = new ControllerScanner(basePackage);
            Set<Class<?>> controllerClasses = controllerScanner.getControllerClassesReflections();
            HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
            // @Controller 클래스 처리
            for (Class<?> controllerClass : controllerClasses) {
                Method[] methods = controllerClass.getDeclaredMethods();
                registry.getHandlerExecution(methods, handlerExecutions);
                // @RequestMapping 메서드 처리
//                    핸들러 정상 등록 확인 (HandlerKey에도 getter 주석 제거해줘야 함)
//                     handlerExecutions.entrySet().stream().map(Map.Entry::getKey)
//                             .map(HandlerKey::getRequestMethod).forEach(System.out::println);
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
