package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) { // 애노테이션 존재 여부 확인
                method.invoke(junit4Test); // 메소드 실행
            }
        }

    }
}
