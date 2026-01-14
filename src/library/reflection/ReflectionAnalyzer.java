package library.reflection;

import library.annotations.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ENHANCED Reflection Analyzer with Custom Annotation Processing
 * Demonstrates comprehensive Java Reflection capabilities
 */
public class ReflectionAnalyzer {
    
    /**
     * Analyze a class with annotation processing
     */
    public static ClassInfo analyzeClass(Class<?> clazz) {
        ClassInfo info = new ClassInfo();
        info.className = clazz.getName();
        info.simpleName = clazz.getSimpleName();
        info.isInterface = clazz.isInterface();
        info.isEnum = clazz.isEnum();
        info.isAbstract = Modifier.isAbstract(clazz.getModifiers());
        info.packageName = clazz.getPackage() != null ? clazz.getPackage().getName() : "";
        
        // Analyze class-level annotations
        info.annotations = analyzeClassAnnotations(clazz);
        
        // Analyze constructors
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            info.constructors.add(analyzeConstructor(constructor));
        }
        
        // Analyze methods (including annotations)
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            info.methods.add(analyzeMethod(method));
        }
        
        // Analyze fields (including annotations)
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            info.fields.add(analyzeField(field));
        }
        
        // Analyze interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> iface : interfaces) {
            info.interfaces.add(iface.getName());
        }
        
        // Analyze superclass
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            info.superclass = superclass.getName();
        }
        
        // Check for inner classes
        Class<?>[] innerClasses = clazz.getDeclaredClasses();
        for (Class<?> inner : innerClasses) {
            info.innerClasses.add(inner.getSimpleName());
        }
        
        // Detect design patterns
        info.designPatterns = detectDesignPatterns(clazz);
        
        // Process custom annotations
        info.customAnnotationInfo = processCustomAnnotations(clazz);
        
        return info;
    }
    
    /**
     * NEW: Analyze class-level annotations
     */
    private static List<AnnotationInfo> analyzeClassAnnotations(Class<?> clazz) {
        List<AnnotationInfo> annotations = new ArrayList<>();
        Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
        
        for (Annotation annotation : classAnnotations) {
            AnnotationInfo info = new AnnotationInfo();
            info.name = annotation.annotationType().getSimpleName();
            info.fullName = annotation.annotationType().getName();
            info.details = annotation.toString();
            annotations.add(info);
        }
        
        return annotations;
    }
    
    /**
     * NEW: Process custom annotations and extract detailed information
     */
    private static String processCustomAnnotations(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        
        // Check for @DesignPattern annotation
        if (clazz.isAnnotationPresent(DesignPattern.class)) {
            DesignPattern dp = clazz.getAnnotation(DesignPattern.class);
            sb.append("\n📐 Design Pattern Information:");
            sb.append("\n   Pattern: ").append(dp.pattern());
            sb.append("\n   Description: ").append(dp.description());
            if (dp.benefits().length > 0) {
                sb.append("\n   Benefits: ").append(Arrays.toString(dp.benefits()));
            }
        }
        
        // Check for @Immutable annotation
        if (clazz.isAnnotationPresent(Immutable.class)) {
            Immutable im = clazz.getAnnotation(Immutable.class);
            sb.append("\n🔒 Immutability Information:");
            sb.append("\n   Status: ").append(im.value());
            sb.append("\n   Thread-Safe: ").append(im.threadSafe());
        }
        
        // Check for @Author annotation
        if (clazz.isAnnotationPresent(Author.class)) {
            Author author = clazz.getAnnotation(Author.class);
            sb.append("\n✍️  Author Information:");
            sb.append("\n   Name: ").append(author.name());
            sb.append("\n   Date: ").append(author.date());
            sb.append("\n   Version: ").append(author.version());
            if (author.modifications().length > 0) {
                sb.append("\n   Modifications: ").append(Arrays.toString(author.modifications()));
            }
        }
        
        return sb.length() > 0 ? sb.toString() : "\n   No custom annotations found.";
    }
    
    /**
     * NEW: Analyze field annotations (especially @Validatable)
     */
    private static FieldInfo analyzeField(Field field) {
        FieldInfo info = new FieldInfo();
        info.name = field.getName();
        info.modifiers = Modifier.toString(field.getModifiers());
        info.type = field.getType().getSimpleName();
        info.isFinal = Modifier.isFinal(field.getModifiers());
        info.isStatic = Modifier.isStatic(field.getModifiers());
        info.isPrivate = Modifier.isPrivate(field.getModifiers());
        
        // NEW: Check for @Validatable annotation
        if (field.isAnnotationPresent(Validatable.class)) {
            Validatable val = field.getAnnotation(Validatable.class);
            info.validationInfo = String.format(
                "Validatable[required=%s, minLength=%d, maxLength=%d, message='%s']",
                val.required(), val.minLength(), val.maxLength(), val.message()
            );
        }
        
        return info;
    }
    
    /**
     * NEW: Analyze method annotations (especially @PerformanceMonitor)
     */
    private static MethodInfo analyzeMethod(Method method) {
        MethodInfo info = new MethodInfo();
        info.name = method.getName();
        info.modifiers = Modifier.toString(method.getModifiers());
        info.returnType = method.getReturnType().getSimpleName();
        info.parameterCount = method.getParameterCount();
        info.isStatic = Modifier.isStatic(method.getModifiers());
        info.isPublic = Modifier.isPublic(method.getModifiers());
        info.isPrivate = Modifier.isPrivate(method.getModifiers());
        
        Parameter[] parameters = method.getParameters();
        for (Parameter param : parameters) {
            info.parameterTypes.add(param.getType().getSimpleName());
        }
        
        // NEW: Check for @PerformanceMonitor annotation
        if (method.isAnnotationPresent(PerformanceMonitor.class)) {
            PerformanceMonitor pm = method.getAnnotation(PerformanceMonitor.class);
            info.performanceInfo = String.format(
                "PerformanceMonitor[operation='%s', logExecution=%s, maxTime=%dms]",
                pm.operationName(), pm.logExecution(), pm.expectedMaxTime()
            );
        }
        
        // Check for @Author annotation on methods
        if (method.isAnnotationPresent(Author.class)) {
            Author author = method.getAnnotation(Author.class);
            info.authorInfo = String.format(
                "Author[name='%s', date='%s', version='%s']",
                author.name(), author.date(), author.version()
            );
        }
        
        return info;
    }
    
    private static ConstructorInfo analyzeConstructor(Constructor<?> constructor) {
        ConstructorInfo info = new ConstructorInfo();
        info.modifiers = Modifier.toString(constructor.getModifiers());
        info.parameterCount = constructor.getParameterCount();
        info.isPrivate = Modifier.isPrivate(constructor.getModifiers());
        info.isPublic = Modifier.isPublic(constructor.getModifiers());
        
        Parameter[] parameters = constructor.getParameters();
        for (Parameter param : parameters) {
            info.parameterTypes.add(param.getType().getSimpleName());
        }
        
        return info;
    }
    
    /**
     * Detect design patterns (enhanced with annotation checking)
     */
    private static List<String> detectDesignPatterns(Class<?> clazz) {
        List<String> patterns = new ArrayList<>();
        
        // Check annotation first
        if (clazz.isAnnotationPresent(DesignPattern.class)) {
            DesignPattern dp = clazz.getAnnotation(DesignPattern.class);
            patterns.add(dp.pattern() + " (Annotated)");
        }
        
        // Check for Singleton pattern
        if (hasSingletonPattern(clazz)) {
            patterns.add("Singleton Pattern");
        }
        
        // Check for Builder pattern
        if (hasBuilderPattern(clazz)) {
            patterns.add("Builder Pattern");
        }
        
        // Check for Observer pattern
        if (hasObserverPattern(clazz)) {
            patterns.add("Observer Pattern");
        }
        
        // Check for Strategy pattern
        if (hasStrategyPattern(clazz)) {
            patterns.add("Strategy Pattern");
        }
        
        // Check for Factory pattern
        if (hasFactoryPattern(clazz)) {
            patterns.add("Factory Pattern");
        }
        
        return patterns;
    }
    
    private static boolean hasSingletonPattern(Class<?> clazz) {
        try {
            Method getInstance = clazz.getMethod("getInstance");
            if (Modifier.isStatic(getInstance.getModifiers()) &&
                getInstance.getReturnType().equals(clazz)) {
                
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                for (Constructor<?> cons : constructors) {
                    if (Modifier.isPrivate(cons.getModifiers())) {
                        return true;
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            // Not a singleton
        }
        return false;
    }
    
    private static boolean hasBuilderPattern(Class<?> clazz) {
        Class<?>[] innerClasses = clazz.getDeclaredClasses();
        for (Class<?> inner : innerClasses) {
            if (inner.getSimpleName().equals("Builder")) {
                try {
                    Method buildMethod = inner.getMethod("build");
                    if (buildMethod.getReturnType().equals(clazz)) {
                        return true;
                    }
                } catch (NoSuchMethodException e) {
                    // Continue checking
                }
            }
        }
        return false;
    }
    
    private static boolean hasObserverPattern(Class<?> clazz) {
        if (clazz.isInterface()) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("update") || 
                    method.getName().equals("notify")) {
                    return true;
                }
            }
        } else {
            String className = clazz.getSimpleName().toLowerCase();
            if (className.contains("observer") || className.contains("listener")) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean hasStrategyPattern(Class<?> clazz) {
        if (clazz.isInterface()) {
            String className = clazz.getSimpleName().toLowerCase();
            if (className.contains("strategy")) {
                return true;
            }
            Method[] methods = clazz.getDeclaredMethods();
            if (methods.length == 1 || methods.length == 2) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean hasFactoryPattern(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if ((methodName.startsWith("create") || methodName.startsWith("make") ||
                 methodName.startsWith("get")) && 
                Modifier.isStatic(method.getModifiers()) &&
                !method.getReturnType().equals(void.class)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Invoke a method dynamically using reflection
     */
    public static Object invokeMethod(Object obj, String methodName, Object... args) 
            throws Exception {
        Class<?> clazz = obj.getClass();
        
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && 
                method.getParameterCount() == args.length) {
                method.setAccessible(true);
                return method.invoke(obj, args);
            }
        }
        throw new NoSuchMethodException("Method not found: " + methodName);
    }
    
    /**
     * Get field value using reflection
     */
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    /**
     * Set field value using reflection
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) 
            throws Exception {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    /**
     * NEW: Validate object using @Validatable annotations
     */
    public static List<String> validateObject(Object obj) {
        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            if (field.isAnnotationPresent(Validatable.class)) {
                Validatable val = field.getAnnotation(Validatable.class);
                field.setAccessible(true);
                
                try {
                    Object value = field.get(obj);
                    
                    if (val.required() && value == null) {
                        errors.add(val.message() + " (Field: " + field.getName() + ")");
                    }
                    
                    if (value instanceof String) {
                        String strValue = (String) value;
                        if (strValue.length() < val.minLength() || 
                            strValue.length() > val.maxLength()) {
                            errors.add(val.message() + " (Field: " + field.getName() + ")");
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("Cannot access field: " + field.getName());
                }
            }
        }
        
        return errors;
    }
    
    // Inner classes for storing analysis results
    
    public static class ClassInfo {
        public String className;
        public String simpleName;
        public String packageName;
        public String superclass;
        public boolean isInterface;
        public boolean isEnum;
        public boolean isAbstract;
        public List<ConstructorInfo> constructors = new ArrayList<>();
        public List<MethodInfo> methods = new ArrayList<>();
        public List<FieldInfo> fields = new ArrayList<>();
        public List<String> interfaces = new ArrayList<>();
        public List<String> innerClasses = new ArrayList<>();
        public List<String> designPatterns = new ArrayList<>();
        public List<AnnotationInfo> annotations = new ArrayList<>(); // NEW
        public String customAnnotationInfo = "";                      // NEW
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════\n");
            sb.append("Class Analysis: ").append(simpleName).append("\n");
            sb.append("═══════════════════════════════════════\n");
            sb.append("Full Name: ").append(className).append("\n");
            sb.append("Package: ").append(packageName).append("\n");
            
            if (superclass != null) {
                sb.append("Extends: ").append(superclass).append("\n");
            }
            
            sb.append("Type: ");
            if (isInterface) sb.append("Interface");
            else if (isEnum) sb.append("Enum");
            else if (isAbstract) sb.append("Abstract Class");
            else sb.append("Class");
            sb.append("\n");
            
            // Show annotations
            if (!annotations.isEmpty()) {
                sb.append("\n📝 Annotations:\n");
                for (AnnotationInfo annotation : annotations) {
                    sb.append("   @").append(annotation.name).append("\n");
                }
            }
            
            // Show custom annotation details
            if (!customAnnotationInfo.isEmpty()) {
                sb.append(customAnnotationInfo).append("\n");
            }
            
            if (!designPatterns.isEmpty()) {
                sb.append("\n🎨 Design Patterns Detected:\n");
                for (String pattern : designPatterns) {
                    sb.append("  ✓ ").append(pattern).append("\n");
                }
            }
            
            sb.append("\n📊 Structure:\n");
            sb.append("  Constructors: ").append(constructors.size()).append("\n");
            sb.append("  Methods: ").append(methods.size()).append("\n");
            sb.append("  Fields: ").append(fields.size()).append("\n");
            
            if (!interfaces.isEmpty()) {
                sb.append("  Implements: ").append(String.join(", ", interfaces)).append("\n");
            }
            
            if (!innerClasses.isEmpty()) {
                sb.append("  Inner Classes: ").append(String.join(", ", innerClasses)).append("\n");
            }
            
            return sb.toString();
        }
    }
    
    public static class AnnotationInfo {
        public String name;
        public String fullName;
        public String details;
    }
    
    public static class ConstructorInfo {
        public String modifiers;
        public int parameterCount;
        public boolean isPrivate;
        public boolean isPublic;
        public List<String> parameterTypes = new ArrayList<>();
    }
    
    public static class MethodInfo {
        public String name;
        public String modifiers;
        public String returnType;
        public int parameterCount;
        public boolean isStatic;
        public boolean isPublic;
        public boolean isPrivate;
        public List<String> parameterTypes = new ArrayList<>();
        public String performanceInfo = "";  // NEW
        public String authorInfo = "";       // NEW
    }
    
    public static class FieldInfo {
        public String name;
        public String modifiers;
        public String type;
        public boolean isFinal;
        public boolean isStatic;
        public boolean isPrivate;
        public String validationInfo = "";  // NEW
    }
}