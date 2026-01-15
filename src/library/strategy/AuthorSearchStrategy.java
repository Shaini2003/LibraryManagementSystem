package library.strategy;

import library.annotations.*;
import library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author Search Strategy - Complete with annotations
 */
@DesignPattern(
    pattern = "Strategy Pattern (Concrete Strategy)",
    description = "Searches books by author name"
)
@Author(name = "Library Team", date = "2025-01-15", version = "1.0")
public class AuthorSearchStrategy implements SearchStrategy {
    
    @Override
    @PerformanceMonitor(
        operationName = "Author Search",
        logExecution = false,
        expectedMaxTime = 100
    )
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
            .filter(book -> book.getAuthor().toLowerCase()
                               .contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }
}