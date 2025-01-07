package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행 -> 완료
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test node = constructor.newInstance();

        final Method[] methods = clazz.getMethods();
        for(Method method: methods){
            if(method.isAnnotationPresent(MyTest.class)){
                method.invoke(node);
            }
        }
    }
}
