# Reflection & Custom Annotations Guide

This project utilizes Java Reflection to provide runtime analysis, architectural validation, and automated features.

## 1. Custom Annotations
We defined five custom annotations in `library.annotations`:

| Annotation | Target | Purpose |
|------------|--------|---------|
| `@DesignPattern` | Class | Documents the pattern used (e.g., "Singleton"). |
| `@Validatable` | Field | Marks fields (like ISBN) that require non-null/length checks. |
| `@PerformanceMonitor` | Method | Flags methods to track execution time. |
| `@Immutable` | Class | Declares that the class is thread-safe/immutable. |
| `@Author` | Class/Method | Metadata about authorship and versioning. |

## 2. Reflection Analyzer
The `library.reflection.ReflectionAnalyzer` class is a utility that inspects the code at runtime.

### Features:
1.  **Pattern Detection:** It scans class structures (constructors, static methods) to automatically detect if the **Singleton** or **Builder** patterns are implemented, even if annotations are missing.
2.  **Annotation Processing:** It reads the `@DesignPattern` annotations to generate architectural reports at runtime.
3.  **Automated Validation:** The `validateObject(Object obj)` method dynamically iterates over all fields marked with `@Validatable` to enforce data integrity rules (e.g., checking if `title` is null).

## 3. Dynamic Method Invocation
Demonstrated in the `demonstrateReflection()` method:
* Using `Method.invoke()` to call methods by name string (e.g., `"getTitle"`).
* Using `Field.setAccessible(true)` to read private variables (like `isbn`) for testing purposes, bypassing encapsulation for verification.

## 4. Testability
Reflection is used heavily in `LibrarySystemTest.java` to verify that refactoring was successful. For example, tests use reflection to assert that the `LibraryService` truly has a private constructor, enforcing the Singleton property.
