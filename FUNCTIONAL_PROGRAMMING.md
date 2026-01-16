# Functional Programming Features

Phase 5 introduced Functional Programming (FP) concepts to modernize the codebase, utilizing Java 21 features.

## 1. Immutability
* **Concept:** Data objects should not be modified in place.
* **Implementation:** The `Book` and `Member` classes are immutable.
* **Example:** `Book.withStatus(newStatus)` does not change the existing book; it returns a **new** `Book` instance with the updated status. This ensures thread safety.

## 2. Java Streams API
We replaced imperative `for` loops with declarative Stream pipelines.

* **Filtering:** `filter(book -> book.getStatus() == AVAILABLE)`
* **Mapping:** `map(Book::getTitle)` extracts titles.
* **Sorting:** `sorted(Comparator.comparing(Book::getAuthor))`
* **Distinct:** `distinct()` removes duplicates from author lists.

## 3. Lambdas & Functional Interfaces
* **Predicates:** `LibraryService.filterBooks(Predicate<Book> p)` allows passing custom filter logic as a lambda.
    * Usage: `service.filterBooks(b -> b.getTitle().contains("Java"))`
* **Consumers:** Used in `forEach` loops for printing/logging.

## 4. Method References
Used to make code more concise:
* `Transaction::isOverdue` instead of `t -> t.isOverdue()`
* `System.out::println` for output.
* `Book::getCategory` for grouping collectors.

## 5. Collectors & Aggregation
Used `java.util.stream.Collectors` for complex data processing:
* **Grouping:** `Collectors.groupingBy(Book::getCategory)` creates a Map of books per category.
* **Counting:** `Collectors.counting()` provides statistics.
* **Collecting:** `Collectors.toList()` gathers stream results.

## 6. Optional
Used to handle null-safety. Methods like `getBook(isbn)` return `Optional<Book>` instead of `Book` (which could be null), forcing the caller to handle the "not found" scenario explicitly using `.ifPresent()` or `.orElse()`.
