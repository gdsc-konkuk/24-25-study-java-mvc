package com.techcourse;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.web.WebApplicationInitializer;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final var dispatcherServlet = new DispatcherServlet(handlerAdapterRegistry, handlerMappingRegistry);

        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
