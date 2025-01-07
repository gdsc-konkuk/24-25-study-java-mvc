package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] mehhods = clazz.getMethods();
        for (Method m : mehhods) {
            Annotation[] annotations = m.getDeclaredAnnotations();
            if(Arrays.stream(annotations).anyMatch(a -> a.annotationType().equals(MyTest.class))){
                m.invoke(clazz.newInstance());
            }
        }
    }
}
