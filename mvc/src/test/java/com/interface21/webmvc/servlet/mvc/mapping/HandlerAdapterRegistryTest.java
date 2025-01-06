package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {
    @DisplayName("어노테이션 기반 어댑터를 찾는다.")
    @Test
    void getAdapter() {
        // given
        final var handlerAdapterRegistry = new HandlerAdapterRegistry();
        Object handler = mock(HandlerExecution.class);
        // when
        HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        // then
        assertThat(adapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}