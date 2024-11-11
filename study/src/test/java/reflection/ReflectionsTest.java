package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다. -> 완료
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        classes.forEach(clazz -> {
            log.info("Class Name: {}", clazz.getSimpleName());
            log.debug("Class Package: {}", clazz.getPackageName());
            log.warn("Class Modifiers: {}", clazz.getModifiers());
        });
    }
}
