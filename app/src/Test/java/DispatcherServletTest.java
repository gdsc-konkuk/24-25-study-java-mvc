import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DispatcherServletTest {
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.interface21.webmvc.servlet");
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();
        dispatcherServlet=new DispatcherServlet(annotationHandlerMapping, manualHandlerMapping);
    }

    @Test
    void AnnotationHandlerMapping() throws Exception {
        final var requestGet = mock(HttpServletRequest.class);
        final var responseGet = mock(HttpServletResponse.class);

        final var requestPost = mock(HttpServletRequest.class);
        final var responsePOST = mock(HttpServletResponse.class);

        when(requestGet.getAttribute("id")).thenReturn("gugu");
        when(requestGet.getRequestURI()).thenReturn("/get-test");
        when(requestGet.getMethod()).thenReturn("GET");

        when(requestPost.getAttribute("id")).thenReturn("gugu");
        when(requestPost.getRequestURI()).thenReturn("/post-test");
        when(requestPost.getMethod()).thenReturn("POST");

        final var modelGet = dispatcherServlet.doDispatchWithAnnotationHandlerMapping(requestGet, responseGet);
        final var modelPost = dispatcherServlet.doDispatchWithAnnotationHandlerMapping(requestPost, responsePOST);
        assertThat(modelGet.get("id")).isEqualTo("gugu");
        assertThat(modelPost.get("id")).isEqualTo("gugu");
    }

    @Test
    void ManualHandlerMapping() throws Exception {
        final var requestGet = mock(HttpServletRequest.class);
        final var responseGet = mock(HttpServletResponse.class);

        final var requestPost = mock(HttpServletRequest.class);
        final var responsePOST = mock(HttpServletResponse.class);

        when(requestGet.getAttribute("id")).thenReturn("gugu");
        when(requestGet.getRequestURI()).thenReturn("/get-test");
        when(requestGet.getMethod()).thenReturn("GET");

        when(requestPost.getAttribute("id")).thenReturn("gugu");
        when(requestPost.getRequestURI()).thenReturn("/post-test");
        when(requestPost.getMethod()).thenReturn("POST");

        final var modelGet = dispatcherServlet.doDispatchWithManualHandlerMapping(requestGet, responseGet);
        final var modelPost = dispatcherServlet.doDispatchWithManualHandlerMapping(requestPost, responsePOST);
        assertThat(modelGet.get("id")).isEqualTo("gugu");
        assertThat(modelPost.get("id")).isEqualTo("gugu");
    }

}