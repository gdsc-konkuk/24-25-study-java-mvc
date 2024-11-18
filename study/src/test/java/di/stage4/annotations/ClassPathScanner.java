package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        Reflections reflections = new Reflections(packageName);

        // add all annotated classes
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));

        return annotatedClasses;
    }
}
