package library.annotations;

import java.lang.annotation.*;

/**
 * Custom Annotation: Marks methods for performance monitoring
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PerformanceMonitor {
    String operationName() default "";
    boolean logExecution() default true;
    int expectedMaxTime() default 1000; // milliseconds
}