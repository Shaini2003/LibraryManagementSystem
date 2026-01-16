# Design Patterns Implementation

This project implements four standard design patterns to solve specific architectural problems.

## 1. Singleton Pattern
* **Type:** Creational
* **Class:** `library.service.LibraryService`
* **Problem:** We need a single shared point of access to the library data (Books, Members). Creating multiple instances would result in split/inconsistent data states.
* **Implementation:**
    * Private constructor `private LibraryService()`.
    * `private static volatile LibraryService instance`.
    * Thread-safe `getInstance()` method using double-checked locking.
* **Benefit:** Ensures data consistency across the entire application.

## 2. Builder Pattern
* **Type:** Creational
* **Classes:** `Book`, `Member`, `Transaction`
* **Problem:** Complex objects with many mandatory and optional parameters lead to "telescoping constructors" that are hard to read.
* **Implementation:**
    * Static inner `Builder` class.
    * Fluent interface methods (e.g., `.title(...)`).
    * `build()` method performs final validation.
* **Benefit:** Improves code readability and allows for immutable object creation.

## 3. Observer Pattern
* **Type:** Behavioral
* **Classes:** `LibraryService` (Subject), `LibraryObserver` (Interface), `ConsoleObserver` (Concrete Observer)
* **Problem:** The core business logic (`LibraryService`) was tightly coupled with the user interface logic (`System.out.println`).
* **Implementation:**
    * `LibraryService` maintains a list of `observers`.
    * When an event occurs (e.g., `BOOK_ADDED`), it calls `notifyObservers()`.
    * `ConsoleObserver` handles the actual printing.
* **Benefit:** Decouples logic from display. We can easily add a `FileLoggerObserver` later without changing the Service code.

## 4. Strategy Pattern
* **Type:** Behavioral
* **Classes:** `SearchStrategy` (Interface), `TitleSearchStrategy`, `AuthorSearchStrategy`, `ISBNSearchStrategy`
* **Problem:** The search logic required multiple almost identical methods (`searchByTitle`, `searchByAuthor`). Adding new search types would require modifying the Service class (violating Open/Closed Principle).
* **Implementation:**
    * `LibraryService.searchBooks()` accepts a `SearchStrategy` object.
    * The strategy encapsulates the specific filtering logic.
* **Benefit:** Algorithms can be switched at runtime; adding a new search type (e.g., "Category") requires no changes to existing code.
