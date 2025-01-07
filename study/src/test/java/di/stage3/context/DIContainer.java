package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스.
 * 클래스 간의 의존성을 주입하고 관리하는 역할을 수행한다.
 */
class DIContainer {

    // 생성된 빈(인스턴스)을 저장하는 집합
    private final Set<Object> beans = new HashSet<>();

    // DI 컨테이너가 관리할 클래스의 집합
    private final Set<Class<?>> classes;

    // 순환 참조를 확인하기 위한 클래스 집합
    private final Set<Class<?>> processing = new HashSet<>();

    /**
     * DIContainer 생성자
     * @param classes DI 컨테이너가 관리할 클래스 집합
     */
    public DIContainer(final Set<Class<?>> classes) {
        this.classes = classes; // 관리할 클래스 저장
        for (Class<?> clazz : classes) { // 클래스 목록을 순회하며
            createBean(clazz); // 각 클래스에 대해 빈 생성
        }
    }

    /**
     * 요청한 클래스 타입에 해당하는 빈(인스턴스)을 반환
     * @param aClass 요청한 클래스 타입
     * @return 요청한 클래스 타입의 빈(인스턴스)
     * @throws IllegalArgumentException 요청한 클래스 타입의 빈이 없을 경우 예외 발생
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream() // 빈 목록에서
                .filter(aClass::isInstance) // 요청한 타입과 일치하는 빈 필터링
                .findFirst() // 첫 번째 매칭된 빈 반환
                .orElseThrow(() -> new IllegalArgumentException("bean 없음: " + aClass)); // 없으면 예외 발생
    }

    /**
     * 클래스 타입을 기반으로 빈(인스턴스)을 생성하고 관리
     * @param clazz 빈을 생성할 클래스 타입
     */
    private void createBean(Class<?> clazz) {
        // 이미 빈이 생성되어 있으면 다시 생성하지 않음
        if (beans.stream().anyMatch(clazz::isInstance)) {
            return;
        }

        // 순환 참조를 감지하여 무한 재귀를 방지
        if (processing.contains(clazz)) {
            throw new IllegalStateException("순환참조: " + clazz.getName());
        }

        // 현재 클래스 타입을 순환 참조 추적 목록에 추가
        processing.add(clazz);

        // 적절한 생성자를 찾음
        Constructor<?> constructor = getSuitableConstructor(clazz);
        if (constructor == null) {
            throw new IllegalStateException("적절한 생성자 없음: " + clazz.getName());
        }

        // 생성자 매개변수 정보를 가져옴
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length]; // 매개변수에 맞는 빈을 저장할 배열

        try {
            // 각 매개변수에 대해 의존성을 해결하여 주입
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> dependencyClass = parameterTypes[i]; // 매개변수 타입
                Object dependency;

                if (dependencyClass.isInterface()) { // 매개변수 타입이 인터페이스인 경우
                    // 해당 인터페이스를 구현한 클래스를 찾음
                    Class<?> implClass = findImplementation(dependencyClass);
                    if (implClass == null) {
                        throw new IllegalStateException("인터페이스의 구현체 없음: " + dependencyClass);
                    }
                    createBean(implClass); // 구현체의 빈 생성
                    dependency = beans.stream()
                            .filter(dependencyClass::isInstance) // 구현체의 빈 필터링
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("인터페이스 구현체 빈 없음: " + dependencyClass));
                } else { // 매개변수 타입이 클래스인 경우
                    createBean(dependencyClass); // 클래스 타입 빈 생성
                    dependency = beans.stream()
                            .filter(dependencyClass::isInstance) // 생성된 클래스 타입의 빈 필터링
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("클래스 타입의 빈 없음: " + dependencyClass));
                }

                // 매개변수 배열에 의존성을 추가
                parameters[i] = dependency;
            }

            // 생성자를 호출하여 인스턴스를 생성
            Object instance = constructor.newInstance(parameters);
            beans.add(instance); // 생성된 인스턴스를 빈 목록에 추가
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("빈 생성 중 오류 발생: " + clazz.getName(), e);
        }

        // 클래스 타입을 순환 참조 추적 목록에서 제거
        processing.remove(clazz);
    }

    /**
     * 클래스 타입에서 가장 적합한 생성자를 선택
     * @param clazz 생성자를 선택할 클래스 타입
     * @return 매개변수 수가 가장 많은 생성자
     */
    private Constructor<?> getSuitableConstructor(Class<?> clazz) {
        // 생성자 배열을 스트림으로 변환하고, 매개변수 수가 많은 순으로 정렬
        return Arrays.stream(clazz.getConstructors()) // 클래스의 생성자 배열을 스트림으로 변환
                .sorted((c1, c2) -> Integer.compare(c2.getParameterCount(), c1.getParameterCount())) // 매개변수 수 기준 내림차순 정렬
                .findFirst() // 가장 매개변수가 많은 생성자를 반환
                .orElse(null); // 생성자가 없으면 null 반환
    }

    /**
     * 특정 인터페이스를 구현한 클래스를 찾아 반환
     * @param interfaceClass 찾을 인터페이스 타입
     * @return 인터페이스를 구현한 클래스
     */
    private Class<?> findImplementation(Class<?> interfaceClass) {
        return classes.stream() // 관리 중인 클래스 목록에서
                .filter(interfaceClass::isAssignableFrom) // 인터페이스를 구현한 클래스 필터링
                .filter(clazz -> !clazz.isInterface()) // 구현체만 필터링
                .findFirst() // 첫 번째 구현체 반환
                .orElse(null); // 없으면 null 반환
    }
}