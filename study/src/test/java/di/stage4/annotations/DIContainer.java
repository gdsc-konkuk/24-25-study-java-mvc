package di.stage4.annotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;
    //왜 여기서 갑자기 classes가 0이 되는건지 모르겠음
    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>(classes);

    }

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}
