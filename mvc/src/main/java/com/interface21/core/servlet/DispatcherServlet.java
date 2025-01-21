package com.interface21.core.servlet;

import com.interface21.web.handler.adapter.HandlerAdapterRegistry;
import com.interface21.web.handler.mapping.HandlerMappingRegistry;
import com.interface21.web.ModelAndView;
import com.interface21.web.handler.adapter.HandlerAdapter;
import com.interface21.web.handler.adapter.HandlerExecutionHandlerAdapter;
import com.interface21.web.handler.mapping.AnnotationHandlerMapping;
import com.interface21.web.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        try {
            handlerMappingRegistry.registerHandlerMapping(AnnotationHandlerMapping.class, "samples");
            handlerAdapterRegistry.registerHandlerAdapter(HandlerExecutionHandlerAdapter.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = processHandler(request, response);
            renderViewWithModel(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView processHandler(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }

    private void renderViewWithModel(
            final ModelAndView modelAndView, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
