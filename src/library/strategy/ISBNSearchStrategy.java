package library.strategy;

import library.annotations.*;
import library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ISBN Search Strategy - Complete with annotations
 */
@DesignPattern(
    pattern = "Strategy Pattern (Concrete Strategy)",
    description = "Searches books by exact ISBN match"
)
@Author(name = "Library Team", date = "2025-01-15", version = "1.0")
public class ISBNSearchStrategy implements SearchStrategy {
    
    @Override
    @PerformanceMonitor(
        operationName = "ISBN Search",
        logExecution = false,
        expectedMaxTime = 50
    )
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
            .filter(book -> book.getIsbn().equals(query))
            .collect(Collectors.toList());
    }
}