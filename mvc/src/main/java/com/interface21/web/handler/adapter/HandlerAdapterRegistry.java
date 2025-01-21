package com.interface21.web.handler.adapter;

import com.interface21.core.util.ReflectionUtils;
import java.util.HashSet;
import java.util.Set;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new HashSet<>();
    }

    public void registerHandlerAdapter(
            final Class<? extends HandlerAdapter> handlerAdapterClass, final Object... args)
            throws ReflectiveOperationException {
        HandlerAdapter handlerAdapter = ReflectionUtils
                .accessibleConstructor(handlerAdapterClass, Object[].class)
                .newInstance((Object) args);
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
