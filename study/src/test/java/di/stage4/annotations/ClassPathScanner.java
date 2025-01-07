package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    /**
     * 패키지 이름을 기반으로 해당 패키지에 포함된 모든 클래스를 반환
     * @param packageName 스캔할 패키지 이름
     * @return 패키지 내 모든 클래스 집합
     */
    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        // 검색할 애너테이션 목록
        Set<Class<?>> annotations = Set.of(Service.class, Repository.class);

        // 모든 애너테이션을 검색하여 결과 합치기
        Set<Class<?>> allClasses = new HashSet<>();
        for (Class<?> annotation : annotations) {
            allClasses.addAll(reflections.getTypesAnnotatedWith((Class) annotation, true));
        }

        return allClasses;
    }
}
