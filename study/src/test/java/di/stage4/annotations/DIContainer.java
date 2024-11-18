package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();

        // 1. create beans
        try{
            for(Class<?> clazz : classes) {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();

                Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Inject.class))
                        .forEach(field -> field.setAccessible(true));

                beans.add(instance);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create instances", e);
        }

        // 2. dependency inject
        for(Object bean : beans) {
            injectDependencies(bean);
        }
    }
    private void injectDependencies(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try{
                    field.set(bean, getBean(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject Dependencies in" + field.getName() + " at " + bean.getClass().getTypeName(), e);
                }
            }
        }
        System.out.println("Successfully injected Dependencies in " + bean.getClass().getTypeName());
    }

    // rootPackageName = di.stage4.annotations
    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> packages = ClassPathScanner.getAllClassesInPackage(rootPackageName);

        return new DIContainer(packages);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        for (Object bean : beans) {
            if (clazz.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        throw new RuntimeException("No bean found for " + clazz.getName());
    }
}
