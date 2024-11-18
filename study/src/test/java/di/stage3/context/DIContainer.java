package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        Map<Class<?>, Object> tempBeans = new HashMap<>();
        try{
            // Create instances without injecting dependencies
            for (Class<?> clazz : classes) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                tempBeans.put(clazz, instance);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create instances", e);
        }

        // Inject dependencies if needed
        for (Map.Entry<Class<?>, Object> entry : tempBeans.entrySet()) {
            Object instance = injectDependenciesIfNeeded(entry.getKey(), entry.getValue(), tempBeans);
            beans.add(instance);
        }
    }

    private Object injectDependenciesIfNeeded(Class<?> clazz, Object instance, Map<Class<?>, Object> tempBeans) {
        Constructor<?> constructor = findSuitableConstructor(clazz);
        if (constructor.getParameterCount() == 0) {
            return instance; // No dependencies to inject
        }

        try {
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = findCompatibleBean(parameterTypes[i], tempBeans);
                if (args[i] == null) {
                    throw new RuntimeException("Dependency not found for parameter of type " + parameterTypes[i].getName() + " in " + clazz.getName());
                }
            }

            return constructor.newInstance(args);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject dependencies for " + clazz.getName(), e);
        }
    }

    private Constructor<?> findSuitableConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        // find constructor that has max parameters
        Optional<Constructor<?>> injectionConstructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterCount() > 0)
                .max(Comparator.comparingInt(Constructor::getParameterCount));

        // if there's no parameters in constructor, return default
        return injectionConstructor.orElseGet(() -> {
            try {
                return clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No suitable constructor found for " + clazz.getName(), e);
            }
        });
    }

    private Object findCompatibleBean(Class<?> type, Map<Class<?>, Object> beans) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }
        return null;
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
