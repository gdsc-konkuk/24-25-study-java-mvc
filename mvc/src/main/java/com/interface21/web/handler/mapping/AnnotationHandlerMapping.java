package com.interface21.web.handler.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.ControllerScanner;
import com.interface21.core.bean.Bean;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.handler.HandlerExecution;
import com.interface21.web.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        controllerScanner.scanControllerClasses(basePackage).forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public boolean supports(final HttpServletRequest request) {
        return getHandler(request) != null;
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlerExecutions(final Class<?> controllerClass) {
        String baseValue = controllerClass.getAnnotation(Controller.class).value();
        Object controllerObject = Bean.getBean(controllerClass);
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .forEach(genRegisterMethod(controllerObject, baseValue));
    }

    private Consumer<Method> genRegisterMethod(final Object controllerObject, final String baseValue) {
        return method -> {
            HandlerExecution handlerExecution = new HandlerExecution(controllerObject, method);

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = baseValue + requestMapping.value();
            for (final RequestMethod m : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(url, m);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.debug("Registered handler `{}` for \"{} {}\"", method.getName(), m, url);
            }
        };
    }
}
