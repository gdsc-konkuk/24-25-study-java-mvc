package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        classes.forEach(aClass -> initBean(aClass, classes));
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream().filter(aClass::isInstance).map(aClass::cast).findFirst().orElseThrow();
    }

    private Optional<Object> getBeanOpt(Class<?> aClass) {
        return beans.stream().filter(aClass::isInstance).findFirst();
    }

    private Class<?> getDescendent(Class<?> aClass, Set<Class<?>> classes) {
        return classes.stream().filter(aClass::isAssignableFrom).findFirst().orElseThrow();
    }

    private Object initDescendent(Class<?> aClass, Set<Class<?>> classes) {
        return initBean(getDescendent(aClass, classes), classes);
    }

    private Object initBean(Class<?> aClass, Set<Class<?>> classes) {
        try {
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            if (constructors.length != 1) {
                throw new IllegalArgumentException(aClass + " 클래스의 생성자가 1개가 아닙니다.");
            }

            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);

            Object[] dependencies =
                    Arrays.stream(constructor.getParameterTypes())
                            .map(type -> getBeanOpt(type).orElseGet(() -> initDescendent(type, classes)))
                            .toArray();

            Object bean = constructor.newInstance(dependencies);
            beans.add(bean);
            return bean;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
