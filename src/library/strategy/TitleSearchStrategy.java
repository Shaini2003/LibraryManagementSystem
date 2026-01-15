package library.strategy;

import library.annotations.*;
import library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Title Search Strategy - Complete with annotations
 */
@DesignPattern(
    pattern = "Strategy Pattern (Concrete Strategy)",
    description = "Searches books by title"
)
@Author(name = "Library Team", date = "2025-01-15", version = "1.0")
public class TitleSearchStrategy implements SearchStrategy {
    
    @Override
    @PerformanceMonitor(
        operationName = "Title Search",
        logExecution = false,
        expectedMaxTime = 100
    )
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase()
                               .contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }
}