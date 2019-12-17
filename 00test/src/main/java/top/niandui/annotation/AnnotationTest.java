package top.niandui.annotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 注解测试
 */
public class AnnotationTest {

    public static void main(String[] args) throws NoSuchFieldException {
        Class c = Test1.class;
        Annotation annotation = c.getAnnotation(NotNull.class);
        Class<? extends Annotation> aClass = annotation.getClass();
        Field message = aClass.getField("message");
        System.out.println(message);
        System.out.println(annotation);
//        System.getProperties().forEach((o, o2) -> {
//            System.out.println(o + ": " + o2);
//        });

    }

    @NotNull(message = "aaaa")
    public static class Test1 {

    }
}
