package di.stage3.context;

import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */

class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            beans.add(clazz.newInstance());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        // Set에서 해당 클래스의 인스턴스를 찾아 반환
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}