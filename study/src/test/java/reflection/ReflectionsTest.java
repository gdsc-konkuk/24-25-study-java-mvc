package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReflectionsTest {

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");
        Set<Class<?>> annotatedClasses= Stream.of(
                reflections.getTypesAnnotatedWith(Controller.class),
                reflections.getTypesAnnotatedWith(Service.class),
                reflections.getTypesAnnotatedWith(Repository.class))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Logger log = LoggerFactory.getLogger(ReflectionsTest.class);
        annotatedClasses.forEach(clazz->log.info(clazz.getName()));
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
