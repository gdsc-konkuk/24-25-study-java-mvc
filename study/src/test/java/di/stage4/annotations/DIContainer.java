package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) {
        // 빈 생성
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class)) {
                createBean(clazz);
            }
        }

        // 의존성 주입
        for (Object bean : beans) {
            injectDependencies(bean);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName); // 패키지 내 클래스 스캔
        return new DIContainer(classes); // 스캔된 클래스로 DI 컨테이너 생성
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("bean 없음 : " + aClass));
    }

    private void createBean(Class<?> clazz) {
        try {
            // 생성자를 가져오고 접근 가능하게 설정
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // private 생성자 접근 가능 설정

            // 생성자를 통해 인스턴스 생성
            Object instance = constructor.newInstance();
            beans.add(instance); // 빈으로 등록
        } catch (Exception e) {
            throw new RuntimeException("빈 생성 중 오류 발생: " + clazz.getName(), e);
        }
    }

    private void injectDependencies(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields(); // 클래스의 모든 필드 가져오기
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) { // @Inject 애너테이션 확인
                Class<?> dependencyType = field.getType(); // 필드의 타입
                Object dependency = getBean(dependencyType); // 의존성 찾기
                try {
                    field.setAccessible(true); // private 필드 접근 가능하도록 설정
                    field.set(bean, dependency); // 필드에 의존성 주입
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("의존성 주입 중 오류 발생: " + field.getName(), e);
                }
            }
        }
    }
}
