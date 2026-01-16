# Refactoring Documentation

This document details the refactoring techniques applied to the Library Management System to eliminate "Bad Smells" and improve code maintainability, adhering to Martin Fowler's principles.

## Summary of Refactorings

| ID | Bad Smell | Refactoring Technique | Applied Class(es) |
|----|-----------|----------------------|-------------------|
| 1 | **Long Parameter List** | **Replace Constructor with Builder** | `Book`, `Member`, `Transaction` |
| 2 | **Magic Numbers / Strings** | **Replace Type Code with Enum** | `Member.MemberType`, `Book.BookStatus` |
| 3 | **God Class** | **Extract Class** | `Transaction` (extracted from `LibraryService` logs) |
| 4 | **Long Method** | **Extract Method** | `LibraryService.borrowBook()` -> `validate...()` |
| 5 | **Exposed Mutable Fields** | **Encapsulate Field** | `Book`, `Member` (fields made `private final`) |
| 6 | **Exposed Collection** | **Encapsulate Collection** | `Member.getBorrowedBookIsbns()` (returns unmodifiable) |
| 7 | **Duplicate Code** | **Form Template Method** | `LibraryService.searchBooks()` (Strategy Pattern) |
| 8 | **Mutable Data** | **Change Value to Reference** | `Book.withStatus()` (Immutability) |

---

## Detailed Refactoring Log

### 1. Replace Constructor with Builder
* **Problem:** The `Book` and `Member` constructors had 5+ parameters, making object creation error-prone and hard to read.
* **Refactoring:** Implemented the **Builder Pattern**.
* **Result:** Client code is now readable:
    ```java
    // Before
    new Book("ISBN", "Title", "Author", "Cat", "Status", Date);
    
    // After
    new Book.Builder().isbn("ISBN").title("Title").build();
    ```

### 2. Replace Type Code with Enum
* **Problem:** `memberType` was a String ("STUDENT", "FACULTY"). Borrowing limits (3, 5) were Magic Numbers hidden in `if` statements.
* **Refactoring:** Created `MemberType` Enum with a field for `maxBooksAllowed`.
* **Result:** Type safety enforced; logic centralized in the Enum.

### 3. Extract Method
* **Problem:** `LibraryService.borrowBook()` was a "Long Method" handling validation, logic, state updates, and logging.
* **Refactoring:** Split into:
    * `validateMemberExists()`
    * `validateBookAvailable()`
    * `executeBorrowOperation()`
* **Result:** Improved readability and testability.

### 4. Encapsulate Collection
* **Problem:** `Member.borrowedBookIsbns` returned a direct reference to the `ArrayList`. External classes could clear the list without checking rules.
* **Refactoring:** Changed getter to return `Collections.unmodifiableList(borrowedBookIsbns)`.
* **Result:** Data integrity is guaranteed. Modifications must go through business logic methods.

### 5. Extract Class
* **Problem:** Transactions were stored as formatted Strings (`"BORROW,M001,ISBN..."`) in a generic list.
* **Refactoring:** Created a dedicated `Transaction` domain class.
* **Result:** Type-safe transaction handling with proper dates and types.
