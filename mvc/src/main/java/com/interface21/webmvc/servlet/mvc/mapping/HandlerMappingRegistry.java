package com.interface21.webmvc.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {
    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.stream()
                .forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
