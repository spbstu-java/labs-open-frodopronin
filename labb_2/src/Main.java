import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyAnnotation {
    int timesToExecute() default 0;
}

class MyClass {
    public int sum(int a, int b) {
        System.out.println("Public Sum!");
        return a + b;
    }

    @MyAnnotation(timesToExecute = 2)
    public double sqrt(double a) {
        System.out.println("Public Sqrt!");
        return Math.sqrt(a);
    }

    public void info() {
        System.out.println("Hello, World!");
    }

    protected int sub(int a, int b) {
        System.out.println("Protected Sub!");
        return a - b;
    }

    @MyAnnotation(timesToExecute = 3)
    protected double div(double a, double b) {
        System.out.println("Protected Div!");
        return a / b;
    }

    @MyAnnotation(timesToExecute = 2)
    protected void superInfo() {
        System.out.println("Protected Info!");
    }

    @MyAnnotation(timesToExecute = 0)
    private int mul(int a, int b) {
        System.out.println("Private Mul!");
        return a * b;
    }

    @MyAnnotation(timesToExecute = 1)
    private double power(double a, double b) {
        System.out.println("Private Power!");
        return Math.pow(a, b);
    }

    private void secretInfo() {
        System.out.println("Private Info!");
    }
}

public class Main {
    private static final Map<Class<?>, Object> DEFAULT_VALUES = Map.of(
            int.class, 5,
            double.class, 7.4,
            float.class, 8.3f,
            boolean.class, true,
            String.class, "class"
    );

    public static void main(String[] args) {
        MyClass obj = new MyClass();

        Arrays.stream(MyClass.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyAnnotation.class))
                .filter(Main::isNonPublicMethod)
                .forEach(method -> executeMethod(obj, method));
    }

    private static boolean isNonPublicMethod(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers);
    }

    private static void executeMethod(MyClass obj, Method method) {
        int timesToExecute = method.getAnnotation(MyAnnotation.class).timesToExecute();
        method.setAccessible(true);

        for (int i = 0; i < timesToExecute; i++) {
            try {
                Object[] parameters = generateParameters(method.getParameterTypes());
                method.invoke(obj, parameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println("Error executing method: " + method.getName());
                e.printStackTrace();
            }
        }
    }

    private static Object[] generateParameters(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(type -> {
                    Object value = DEFAULT_VALUES.get(type);
                    if (value == null) {
                        throw new RuntimeException("Parameter type " + type.getName() + " not supported!");
                    }
                    return value;
                })
                .toArray();
    }
}