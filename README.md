# Library Management System
### Advanced Software Engineering Topics - Complete Implementation

## 📋 Project Information
* **Module Code:** 6CS002 - Advanced Software Engineering Topics
* **Module Title:** Advanced Software Engineering Topics
* **Assessment Type:** Portfolio (100%)
* **Academic Year:** 2026
* **Submission Date:** January 16, 2026
* **Institution:** University of Wolverhampton

## 🎯 Project Overview
A comprehensive Library Management System demonstrating all five required aspects of advanced software engineering:

* ✅ **Bad Smells** - Identification and elimination (23+ smells)
* ✅ **Refactoring** - 12+ refactoring techniques applied
* ✅ **Design Patterns** - 4 patterns fully implemented
* ✅ **Reflection** - Advanced reflection with 5 custom annotations
* ✅ **Functional Programming** - Comprehensive FP features (15+ methods)

**Project Type:** Console-based Java Application
**Total Files:** 18 source files + 1 test file + 7 documentation files
**Lines of Code:** 3,500+
**Total Classes:** 19
---

## 💻 Development Environment

### Java Version
* **Version:** Java 21.0.5 LTS (2024-10-15)
* **Runtime:** Java(TM) SE Runtime Environment (build 21.0.5+9-LTS-239)

### Integrated Development Environment (IDE)
* **Primary IDE:** Visual Studio Code (VS Code) v1.85+
* **Required Extensions:**
    * Extension Pack for Java (Microsoft)
    * Debugger for Java (Microsoft)
    * Test Runner for Java (Microsoft)

### Testing Framework
* **Framework:** JUnit 5.9.3
* **Coverage:** 100% of critical functionality (17 tests)

---

## 🏗️ Project Structure

```text
LibraryManagementSystem/
├── src/
│   └── library/
│       ├── LibraryManagementSystem.java          (Main Application)
│       │
│       ├── annotations/                          (5 Custom Annotations)
│       │   ├── Author.java
│       │   ├── DesignPattern.java
│       │   ├── Immutable.java
│       │   ├── PerformanceMonitor.java
│       │   └── Validatable.java
│       │
│       ├── model/                                (Domain Models)
│       │   ├── Book.java                         (Builder Pattern, Immutable)
│       │   ├── Member.java                       (Builder Pattern, Immutable)
│       │   └── Transaction.java                  (Builder Pattern, Immutable)
│       │
│       ├── service/                              (Business Logic)
│       │   └── LibraryService.java               (Singleton, Observer, FP)
│       │
│       ├── observer/                             (Observer Pattern)
│       │   ├── LibraryObserver.java              (Interface)
│       │   └── ConsoleObserver.java              (Concrete Observer)
│       │
│       ├── strategy/                             (Strategy Pattern)
│       │   ├── SearchStrategy.java               (Interface)
│       │   ├── TitleSearchStrategy.java          (Concrete Strategy)
│       │   ├── AuthorSearchStrategy.java         (Concrete Strategy)
│       │   └── ISBNSearchStrategy.java           (Concrete Strategy)
│       │
│       └── reflection/                           (Reflection)
│           └── ReflectionAnalyzer.java           (Enhanced with Annotations)
│
├── test/
│   └── library/
│       └── test/
│           └── LibrarySystemTest.java            (JUnit 5 Tests - 16 tests)
│
├── docs/                                         (Documentation)
│   ├── BAD_SMELLS.md
│   ├── REFACTORINGS.md
│   ├── REPORT.md
│   └── ...
├── README.md
└── bin/                                          (Compiled classes)
```
## 🚀 How to Run the Project

### Prerequisites
* **Java JDK 21.0.5** or higher installed.
* **Visual Studio Code** (recommended) with the **Extension Pack for Java**.

### Method 1: Running in VS Code (Recommended)
1.  Open the `LibraryManagementSystem` folder in **VS Code**.
2.  Wait for the Java extensions to load the project structure.
3.  Navigate to `src/library/LibraryManagementSystem.java` in the explorer.
4.  Click the **Run** button (▶️) that appears above the `main` method or press **F5**.
5.  The application will start in the **Terminal** panel at the bottom.
