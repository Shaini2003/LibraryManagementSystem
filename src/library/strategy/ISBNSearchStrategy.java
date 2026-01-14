package library.strategy;

import library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Phase 3 - Commit 2: Concrete Strategy for ISBN Search
 */
public class ISBNSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
            .filter(book -> book.getIsbn().equals(query))
            .collect(Collectors.toList());
    }
}