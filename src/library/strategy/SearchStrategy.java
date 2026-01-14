package library.strategy;

import library.model.Book;
import java.util.List;

/**
 * Phase 3 - Commit 2: Strategy Pattern Interface
 * Defines contract for different search algorithms
 */
public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}