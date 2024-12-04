package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private Reflections reflections;

    public ControllerScanner(final Reflections reflections){
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers(){
        final Set<Class<?>> controllerClasses = this.reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for(Class<?> controller : controllerClasses){
            controllers.put(controller, createController(controller));
        }
        return controllers;
    }

    private Object createController(Class<?> controller){
        try{
            return controller.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
