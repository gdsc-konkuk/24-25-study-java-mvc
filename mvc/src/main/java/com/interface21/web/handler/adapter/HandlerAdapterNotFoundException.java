package com.interface21.web.handler.adapter;

public class HandlerAdapterNotFoundException extends IllegalArgumentException {

    public HandlerAdapterNotFoundException() {
        super("No handler adapter found for this handler");
    }
}
