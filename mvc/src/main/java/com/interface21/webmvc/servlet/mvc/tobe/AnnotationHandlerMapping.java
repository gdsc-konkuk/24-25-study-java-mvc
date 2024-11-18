package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws ClassNotFoundException {
        for (Object basePackageObj : basePackage) {
            String basePackage = (String) basePackageObj;  // 예: "samples"
            URL packageURL = Thread.currentThread().getContextClassLoader().getResource(basePackage);
            if (packageURL == null) {
                return;
            }
            File directory = new File(packageURL.getFile());
            if (directory.exists() && directory.isDirectory()) {
                findClasses(directory, basePackage);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName());
                continue;
            }
            if (file.getName().endsWith(".class")) {
                processClassWhenFound(packageName, file, classes);
            }
        }
    }

    private void processClassWhenFound(String packageName, File file, List<Class<?>> classes) throws ClassNotFoundException {
        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
        Class<?> clazz = Class.forName(className);

        if (clazz.isAnnotationPresent(Controller.class)) {
            classes.add(clazz);
            log.info("Found Controller annotation in class: {}", clazz.getName());
            Method[] methods = clazz.getDeclaredMethods();

            // Handler Execution 객체를 만들기위한 controllerInstance의 객체가져오기
            final Object controllerInstance = getControllerInstance(clazz);
            filterAndProcessMethods(methods, controllerInstance);
        }
    }


    private void filterAndProcessMethods(Method[] methods, Object controllerInstance) {
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> initMethodsToHandler(method, controllerInstance));
    }

    private void initMethodsToHandler(Method method, Object controllerInstance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Arrays.stream(requestMapping.method()).forEach(c -> {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), c);
            HandlerExecution handlerExecution = new HandlerExecution(method, controllerInstance);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    private static Object getControllerInstance(Class<?> clazz) {
        Object controllerInstance = null;
        try {
            controllerInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to instantiate controller: " + clazz.getName(), e);
        }
        return controllerInstance;
    }


    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        return this.handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method)));
    }
}
