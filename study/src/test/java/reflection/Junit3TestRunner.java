package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (var method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
