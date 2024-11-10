package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    @DisplayName("Annotation 찾기")
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<?> classWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        Set<?> classWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        Set<?> classWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);

        classWithControllerAnnotation.forEach(c -> {
            log.info("Controller: " + c.toString());
        });
        classWithServiceAnnotation.forEach(c -> {
            log.info("Service: " + c.toString());
        });
        classWithRepositoryAnnotation.forEach(c -> {
            log.info("Repository: " + c.toString());
        });
    }
}
