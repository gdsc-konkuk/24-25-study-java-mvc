package com.interface21.web.handler.mapping;

public class HandlerMappingNotFoundException extends IllegalArgumentException {

    public HandlerMappingNotFoundException() {
        super("No handler mapping found for this request");
    }
}
