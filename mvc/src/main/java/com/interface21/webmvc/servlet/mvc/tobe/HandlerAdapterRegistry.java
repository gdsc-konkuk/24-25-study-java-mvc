package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(){
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerAdapter handlerAdapter){
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdater(final Object handler){
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}
