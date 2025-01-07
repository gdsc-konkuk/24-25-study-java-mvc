package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

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
            for (Object basePackage : basePackage) {
                Set<Class<?>> classes = findClassesInPackage((String) basePackage);

                for (Class<?> clazz : classes) {
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);

                            RequestMethod[] methods = mapping.method();
                            for (RequestMethod requestMethod : methods) {
                                HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                                HandlerExecution execution = new HandlerExecution(clazz, method);
                                handlerExecutions.put(key, execution);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize handler mapping", e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(key);
    }
    private Set<Class<?>> findClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
        String path = packageName.replace('.', '/');
        URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(path);

        if (packageUrl == null) {
            throw new IllegalArgumentException("Package not found: " + packageName);
        }

        File directory = new File(packageUrl.getFile());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid package: " + packageName);
        }

        return findClasses(directory, packageName);
    }

    private Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        return Set.of(directory.listFiles())
                .stream()
                .filter(file -> file.getName().endsWith(".class"))
                .map(file -> {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        log.error("Class not found: " + className, e);
                        return null;
                    }
                })
                .filter(clazz -> clazz != null)
                .collect(Collectors.toSet());
    }
}
