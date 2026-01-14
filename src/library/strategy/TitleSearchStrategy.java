package library.strategy;

import library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Phase 3 - Commit 2: Concrete Strategy for Title Search
 */
public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase()
                               .contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }
}