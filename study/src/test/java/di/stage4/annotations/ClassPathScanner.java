package di.stage4.annotations;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {
        //해당 패키지에 있는 어노테이션들 @Inject, @Service, @Repository 을 찾아서, 이를  set으로 반환해주는 역할
        //Service를 가져와서, inject에 Repository를 DI
    public static Set<Class<?>> getAllClassesInPackage(final String packageName) throws Exception {
        // Reflections 라이브러리를 사용해 어노테이션 검색
        Reflections reflections = new Reflections(packageName);

        // @Service와 @Repository 어노테이션이 붙은 클래스 찾기
        Set<Class<?>> servicesSet = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repoSet = reflections.getTypesAnnotatedWith(Repository.class);

        // @Repository 객체 생성 및 저장
        Set<Object> repoInstances = new HashSet<>();
        for (Class<?> repo : repoSet) {
            Constructor<?> repoConstructor = repo.getDeclaredConstructor();
            Object repoInstance = repoConstructor.newInstance();
            repoInstances.add(repoInstance);
        }

        // @Service 객체 생성 및 @Inject 처리
        for (Class<?> service : servicesSet) {
            Constructor<?> constructor = service.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object serviceInstance = constructor.newInstance();

            for (Method method : service.getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(Inject.class)) {
                    // @Inject 메서드의 첫 번째 파라미터 타입 확인
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == 1) {
                        Class<?> paramType = paramTypes[0];

                        // @Repository로 생성된 객체 중 해당 타입과 일치하는 객체 주입
                        for (Object repoInstance : repoInstances) {
                            if (paramType.isAssignableFrom(repoInstance.getClass())) {
                                method.invoke(serviceInstance, repoInstance);
                                System.out.println(
                                        "Injected " + repoInstance.getClass().getSimpleName() +
                                                " into " + service.getSimpleName() +
                                                " using method " + method.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }

        return servicesSet; // @Service 클래스를 반환
    }
}
