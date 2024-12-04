package com.interface21;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

//컨트롤러 어노테이션 찾기
public class ControllerScanner {
    private final Object[] basePackage;

    public ControllerScanner(Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public Set<Class<?>> getControllerClassesReflections() {
        return Arrays.stream(basePackage)
                .map(Reflections::new)
                .flatMap(o->o.getTypesAnnotatedWith(Controller.class).stream())
                .collect(Collectors.toSet());
    }
}
