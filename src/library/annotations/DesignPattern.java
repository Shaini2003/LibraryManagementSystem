package library.annotations;

import java.lang.annotation.*;

/**
 * Custom Annotation: Marks a class as following a specific design pattern
 * Used for reflection-based pattern detection
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DesignPattern {
    String pattern();
    String description() default "";
    String[] benefits() default {};
}