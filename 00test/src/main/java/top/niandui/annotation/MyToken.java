package top.niandui.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：
 *      token
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyToken {
}
