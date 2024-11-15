package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // Junit3Test에서 이름이 "test"로 시작하는 모든 메소드를 실행
        //Class<?> clazz = Class.forName("reflection.Junit3Test");
        //이 코드로도 작동함. reflection패키지에서, Junit3Test클래스를 찾게끔 작동.

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
