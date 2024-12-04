package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(){
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize(){
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void RegisterHandlerMapping(HandlerMapping handlermapping){
        handlerMappings.add(handlermapping);
    }

    public Object getHandler(HttpServletRequest request){
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}
