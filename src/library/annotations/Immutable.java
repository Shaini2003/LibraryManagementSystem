package library.annotations;

import java.lang.annotation.*;

/**
 * Custom Annotation: Marks a class as immutable
 * Used in functional programming paradigm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Immutable {
    String value() default "This class is immutable";
    boolean threadSafe() default true;
}