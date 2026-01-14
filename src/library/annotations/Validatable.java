package library.annotations;

import java.lang.annotation.*;

/**
 * Custom Annotation: Marks a field that requires validation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Validatable {
    boolean required() default true;
    String message() default "Field validation failed";
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;
}