package library.annotations;

import java.lang.annotation.*;

/**
 * Custom Annotation: Documents the author and version information
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Author {
    String name();
    String date();
    String version() default "1.0";
    String[] modifications() default {};
}