package di.stage4.annotations;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) throws Exception {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(Object.class);
    }
}
