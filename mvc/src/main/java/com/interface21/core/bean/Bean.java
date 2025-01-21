package com.interface21.core.bean;

import com.interface21.context.stereotype.ControllerScanner;
import com.interface21.core.util.ReflectionUtils;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean {

    private static final Logger log = LoggerFactory.getLogger(Bean.class);

    private static final Bean instance;

    static {
        instance = new Bean();
        ControllerScanner controllerScanner = new ControllerScanner();
        controllerScanner.scanControllerClasses("com.techcourse", "samples")
                .forEach(Bean::registerBean);

        log.info("Initialized Bean!");
    }

    private final Map<String, Object> beans = new HashMap<>();

    private Bean() {
    }

    public static void initialize() {
    }

    public static Object getBean(final Class<?> targetClass) {
        return getBean(targetClass.getSimpleName());
    }

    public static Object getBean(final String beanName) {
        Object found = instance.beans.get(beanName);
        if (found == null) {
            throw new BeanNotFoundException();
        }
        return found;
    }

    public static void registerBean(final Class<?> beanClass) {
        registerBean(beanClass.getSimpleName(), beanClass);
    }

    // TODO: support for constructor with arguments
    public static void registerBean(final String beanName, final Class<?> beanClass) { // , Object... args) {
        Object beanObject = instantiateBean(beanClass);
        instance.beans.merge(beanName, beanObject, (oldValue, newValue) -> {
            throw new IllegalArgumentException("Bean already exists");
        });
    }

    // TODO: support for constructor with arguments
    private static Object instantiateBean(final Class<?> beanClass) { // , Object... args) {
        try {
            return ReflectionUtils.accessibleConstructor(beanClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
