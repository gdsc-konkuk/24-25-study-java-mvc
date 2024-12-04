package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Map<Class<?>, Object> controllers = new ControllerScanner(reflections).getControllers();

        for(Class<?> controller : controllers.keySet()){
            final Set<Method> methods = getMethods(controller);
            for(Method method : methods){
                putHandlerExecution(method, controllers.get(controller));
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request){
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    private static Set<Method> getMethods(final Class<?> controller){
        return ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
    }
    private static List<HandlerKey> getHandlerKeys(final RequestMapping requestMapping){
        if(requestMapping != null){
            return Arrays.stream(requestMapping.method())
                    .map(method -> new HandlerKey(requestMapping.value(), method))
                    .collect(toList());
        }
        return new ArrayList<>();
    }
    private static Object getHandler(final Method method) {
        try{
            return method.getDeclaringClass().getConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException |
            NoSuchMethodException e){
            throw new RuntimeException();
        }
    }
    private void putHandlerExecution(Method method, Object handler) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping);

        for(HandlerKey handlerKey : handlerKeys){
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }

    }
}
