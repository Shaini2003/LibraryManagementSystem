package library.reflection;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Phase 4 - Commit 1: Reflection Analyzer
 * Comprehensive Java Reflection implementation
 * - Analyzes class structure
 * - Detects design patterns automatically
 * - Provides dynamic method invocation
 * - Enables runtime introspection
 */
public class ReflectionAnalyzer {
    
    /**
     * Analyze a class and return comprehensive information
     */
    public static ClassInfo analyzeClass(Class<?> clazz) {
        ClassInfo info = new ClassInfo();
        info.className = clazz.getName();
        info.simpleName = clazz.getSimpleName();
        info.isInterface = clazz.isInterface();
        info.isEnum = clazz.isEnum();
        info.isAbstract = Modifier.isAbstract(clazz.getModifiers());
        info.packageName = clazz.getPackage() != null ? clazz.getPackage().getName() : "";
        
        // Analyze constructors
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            info.constructors.add(analyzeConstructor(constructor));
        }
        
        // Analyze methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            info.methods.add(analyzeMethod(method));
        }
        
        // Analyze fields
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
        
        return info;
    }
    
    private static FieldInfo analyzeField(Field field) {
        FieldInfo info = new FieldInfo();
        info.name = field.getName();
        info.modifiers = Modifier.toString(field.getModifiers());
        info.type = field.getType().getSimpleName();
        info.isFinal = Modifier.isFinal(field.getModifiers());
        info.isStatic = Modifier.isStatic(field.getModifiers());
        info.isPrivate = Modifier.isPrivate(field.getModifiers());
        return info;
    }
    
    /**
     * Automatically detect design patterns used in the class
     */
    private static List<String> detectDesignPatterns(Class<?> clazz) {
        List<String> patterns = new ArrayList<>();
        
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
            // Look for getInstance method
            Method getInstance = clazz.getMethod("getInstance");
            if (Modifier.isStatic(getInstance.getModifiers()) &&
                getInstance.getReturnType().equals(clazz)) {
                
                // Look for private constructor
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
                // Check if Builder has a build() method
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
                // Observer pattern typically has update or notify method
                if (method.getName().equals("update") || 
                    method.getName().equals("notify")) {
                    return true;
                }
            }
        } else {
            // Check if class implements Observer-like interface
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
            // Strategy pattern typically has one main method
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
        
        // Find method with matching name and parameter count
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
     * Create instance of a class using default constructor
     */
    public static Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
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
    }
    
    public static class FieldInfo {
        public String name;
        public String modifiers;
        public String type;
        public boolean isFinal;
        public boolean isStatic;
        public boolean isPrivate;
    }
}