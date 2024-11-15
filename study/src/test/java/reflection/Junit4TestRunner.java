package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] methods= clazz.getDeclaredMethods();
        for (Method method : methods) {//for순회하면서
            if(method.isAnnotationPresent(MyTest.class)){
                //method.invoke(clazz.newInstance());
                //ㄴ아래와 동일한 결과 반환하지만, 기본 생성자가 없을 때 동작이 불안하다고 함
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
